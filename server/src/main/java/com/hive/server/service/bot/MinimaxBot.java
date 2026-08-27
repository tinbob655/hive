package com.hive.server.service.bot;

import com.hive.server.model.board.Board;
import com.hive.server.model.board.HexCoordinate;
import com.hive.server.model.board.Piece;
import com.hive.server.model.enums.Bug;
import com.hive.server.model.enums.Colour;
import com.hive.server.model.move.Move;
import com.hive.server.model.move.PlaceMove;
import com.hive.server.model.move.RelocateMove;
import com.hive.server.model.move.SkipMove;
import com.hive.server.model.state.GameState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Primary
@RequiredArgsConstructor
public final class MinimaxBot implements Bot {

    private static final int MINIMAX_DEPTH = 2;


    @Override
    public @NonNull Move decideMove(GameState state) {

        try {

            //make sure we can go
            List<Move> moves = this.sortedLegalMoves(state, true);
            if (moves.isEmpty()) {
                return new SkipMove(Colour.BLACK);
            }

            //we can go, do minimax on the moves
            int bestScore = Integer.MIN_VALUE;
            Move bestMove = moves.getFirst();

            for (Move move : moves) {
                GameState nextState = this.fakeAdvance(state, move);
                int score = this.minimax(nextState, MINIMAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
            return bestMove;
        }

        //in case of any errors just skip the turn and log
        catch (Exception e) {
            System.err.println(e.getMessage());
            return new SkipMove(Colour.BLACK);
        }

    }

    private int minimax(@NonNull GameState state, int depth, int alpha, int beta, boolean maximizing) {

        //if we are done
        if (depth == 0 || state.isGameOver()) {
            return Scorer.score(state);
        }

        List<Move> moves = this.sortedLegalMoves(state, maximizing);

        //no moves result in early return
        if (moves.isEmpty()) return Scorer.score(state);

        int overallScore;

        if (maximizing) {
            overallScore = Integer.MIN_VALUE;
            for (Move move : moves) {
                GameState nextState = this.fakeAdvance(state, move);
                int score = this.minimax(nextState, depth - 1, alpha, beta, false);

                if (score > overallScore) {
                    overallScore = score;
                    alpha = score;
                }

                if (beta <= alpha) {
                    break;
                }
            }
        }

        else {
            overallScore = Integer.MAX_VALUE;
            for (Move move : moves) {
                GameState nextState = this.fakeAdvance(state, move);
                int score = this.minimax(nextState, depth - 1, alpha, beta, true);

                if (score < overallScore) {
                    overallScore = score;
                    beta = score;
                }

                if (beta <= alpha) {
                    break;
                }
            }
        }
        return overallScore;
    }

    private @NonNull List<Move> sortedLegalMoves(@NonNull GameState state, boolean maximising) {
        List<Move> temp = state.legalMoves().stream()
                .sorted(Comparator.comparingInt(mv -> {
                    GameState nextState = this.fakeAdvance(state, mv);
                    return Scorer.score(nextState);
                }))
                .toList();
        return maximising ? temp.reversed() : temp;
    }

    private @NonNull GameState fakeAdvance(@NonNull GameState oldState, @NonNull Move move) {
        Board newBoard = oldState.board().copy();
        switch (move) {
            case PlaceMove mv -> newBoard.addPiece(new Piece(mv.bug(), mv.colour()), mv.destination());
            case RelocateMove mv -> newBoard.movePiece(mv.from(), mv.to());
            case SkipMove ignored -> {}
        }
        Colour newColour = oldState.currentColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
        return new GameState(newBoard, newColour);
    }

    private static class Scorer {

        //should outweigh anything else
        private static final int WIN_SCORE = 1_000_000;

        //heuristic weights
        private static final int QUEEN_LIBERTY_WEIGHT = 40;          //per occupied neighbour of a queen, squared (see below)
        private static final int BEETLE_ON_TOP_OF_QUEEN_WEIGHT = 25; //extra danger for a beetle literally sat on the queen
        private static final int PINNED_PIECE_WEIGHT = 15;           //penalty per own piece that currently cannot move at all
        private static final int MOBILITY_WEIGHT = 3;                //reward per own piece that's free to move
        private static final int BANK_WEIGHT = 2;                    //small reward per bug still held in reserve

        private static final Map<Bug, Integer> STARTING_COUNTS = Map.of(
                Bug.BEE, 1, Bug.BEETLE, 2, Bug.GRASSHOPPER, 3, Bug.SPIDER, 2,
                Bug.ANT, 3, Bug.LADYBUG, 1, Bug.MOSQUITO, 1, Bug.WOODLOUSE, 1
        );

        //gives a game state a score based on how favourable it is
        public static int score(@NonNull GameState state) {
            int res;

            if (state.isGameOver()) {
                boolean whiteLost = isQueenSurrounded(state.board(), Colour.WHITE);
                boolean blackLost = isQueenSurrounded(state.board(), Colour.BLACK);

                if (whiteLost && blackLost) res = 0;    //both surrounded, probably won't happen
                else if (whiteLost) res = WIN_SCORE;    //good for black
                else res = -WIN_SCORE;                  //good for white
            }
            else {
                res = 0;
                res += queenSafetyScore(state.board(), Colour.WHITE);   //white's queen in danger is good for black
                res -= queenSafetyScore(state.board(), Colour.BLACK);

                res += pieceActivityScore(state.board(), Colour.BLACK); //black's own pieces being active is good for black
                res -= pieceActivityScore(state.board(), Colour.WHITE);
            }

            //flip if we actually wanted to score for white
            return state.currentColour() == Colour.BLACK ? res : -res;
        }

        //higher number for a queen being in more danger
        private static int queenSafetyScore(Board board, Colour queenColour) {
            Optional<HexCoordinate> queenAt = board.findPiece(new Piece(Bug.BEE, queenColour));
            if (queenAt.isEmpty()) return 0;

            int occupiedNeighbours = 0;
            for (HexCoordinate neighbour : board.neighbours(queenAt.get())) {
                if (board.isOccupied(neighbour)) occupiedNeighbours++;
            }

            //count is squared since more surrounding of the queen is much more dangerous
            int res = occupiedNeighbours * occupiedNeighbours * QUEEN_LIBERTY_WEIGHT;

            //a beetle sitting directly on top of the queen doesn't add a neighbour, but it's still a serious threat
            if (board.stackHeight(queenAt.get()) > 1) res += BEETLE_ON_TOP_OF_QUEEN_WEIGHT;

            return res;
        }

        //rewards a colour for having active, useful pieces: mobile pieces on the board, and flexibility still held in the bank
        private static int pieceActivityScore(Board board, Colour colour) {
            int res = 0;
            Map<Bug, Integer> placedCounts = new EnumMap<>(Bug.class);

            for (Map.Entry<HexCoordinate, Deque<Piece>> cell : board.getCells().entrySet()) {
                HexCoordinate coord = cell.getKey();
                Deque<Piece> stack = cell.getValue();
                if (stack.isEmpty()) continue;

                //count every piece of this colour in the stack, buried or not, so the bank total below stays accurate
                for (Piece piece : stack) {
                    if (piece.colour() == colour) placedCounts.merge(piece.bug(), 1, Integer::sum);
                }

                //only the top piece of a stack can ever move
                Piece top = stack.peekFirst();
                if (top == null || top.colour() != colour) continue;

                //pinned pieces are bad, mobile ones are good
                boolean pinned = board.willBreakHive(coord);
                res += pinned ? -PINNED_PIECE_WEIGHT : MOBILITY_WEIGHT;
            }

            //pieces in the hand are worth slightly less than those in play
            for (Bug bug : Bug.values()) {
                int placed = placedCounts.getOrDefault(bug, 0);
                int remaining = STARTING_COUNTS.get(bug) - placed;
                res += remaining * BANK_WEIGHT;
            }

            return res;
        }

        private static boolean isQueenSurrounded(@NonNull Board board, Colour colour) {
            Optional<HexCoordinate> queenAt = board.findPiece(new Piece(Bug.BEE, colour));
            if (queenAt.isEmpty()) return false;

            for (HexCoordinate neighbour : board.neighbours(queenAt.get())) {
                if (!board.isOccupied(neighbour)) return false;
            }
            return true;
        }
    }
}