package com.hive.server.service.bot;

import com.hive.server.model.board.Board;
import com.hive.server.model.board.HexCoordinate;
import com.hive.server.model.board.Piece;
import com.hive.server.model.enums.Bug;
import com.hive.server.model.enums.Colour;
import com.hive.server.model.move.Move;
import com.hive.server.model.move.PlaceMove;
import com.hive.server.model.move.RelocateMove;
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

    private static final int SEARCH_DEPTH = 4;

    //hard cap on how long the bot is allowed to think for, in milliseconds
    private static final long TIME_LIMIT_MS = 4_000;

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

    private record ScoredMove(Move move, GameState nextState, int score) {}


    //will be thrown if we run out of time doing minimax
    private static final class SearchTimeout extends RuntimeException {
        private SearchTimeout() { super(null, null, false, false); }
    }
    private static final SearchTimeout TIMEOUT = new SearchTimeout();

    @Override
    public @NonNull Move decideMove(@NonNull GameState state) {

        List<ScoredMove> sortedMoves = this.sortMoves(state.legalMoves(), state, true);
        Move bestMoveOverall = sortedMoves.getFirst().move();
        long deadline = System.currentTimeMillis() + TIME_LIMIT_MS;

        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            try {
                bestMoveOverall = this.bestMoveAtDepth(sortedMoves, depth, deadline);
            }
            catch (SearchTimeout timeout) {
                System.err.println("Timeout reached");
                break;
            }
        }

        return bestMoveOverall;
    }

    private Move bestMoveAtDepth(List<ScoredMove> sortedMoves, int depth, long deadline) {
        Move bestMove = sortedMoves.getFirst().move();
        int bestScore = Integer.MIN_VALUE;

        for (ScoredMove sm : sortedMoves) {
            int eval = this.minimax(sm.nextState(), depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, deadline);
            if (eval > bestScore) {
                bestScore = eval;
                bestMove = sm.move();
            }
        }

        return bestMove;
    }

    private int minimax(GameState state, int depth, int alpha, int beta, boolean maximising, long deadline) throws SearchTimeout {

        if (depth == 0 || state.isGameOver()) {
            return score(state);
        }

        //if we are out of time then tap out
        if (System.currentTimeMillis() >= deadline || Thread.currentThread().isInterrupted()) {
            throw TIMEOUT;
        }

        Set<Move> legalMoves = state.legalMoves();
        List<ScoredMove> sortedMoves = this.sortMoves(legalMoves, state, maximising);

        if (maximising) {

            int maxScore = Integer.MIN_VALUE;
            for (ScoredMove sm : sortedMoves) {
                int eval = this.minimax(sm.nextState(), depth - 1, alpha, beta, false, deadline);
                maxScore = Math.max(maxScore, eval);
                alpha = Math.max(alpha, maxScore);

                if (beta <= alpha) {
                    break;
                }
            }
            return maxScore;
        }

        else {

            int minScore = Integer.MAX_VALUE;
            for (ScoredMove sm : sortedMoves) {
                int eval = this.minimax(sm.nextState(), depth - 1, alpha, beta, true, deadline);
                minScore = Math.min(minScore, eval);
                beta = Math.min(beta, minScore);

                if (beta <= alpha) {
                    break;
                }
            }
            return minScore;
        }
    }

    //gives a game state a score based on how favourable it is
    private int score(@NonNull GameState state) {
        int res;

        if (state.isGameOver()) {
            boolean whiteLost = this.isQueenSurrounded(state.board(), Colour.WHITE);
            boolean blackLost = this.isQueenSurrounded(state.board(), Colour.BLACK);

            if (whiteLost && blackLost) res = 0;    //both surrounded, probably won't happen
            else if (whiteLost) res = WIN_SCORE;    //good for black
            else res = -WIN_SCORE;                  //good for white
        }
        else {
            res = 0;
            res += this.queenSafetyScore(state.board(), Colour.WHITE);   //white's queen in danger is good for black
            res -= this.queenSafetyScore(state.board(), Colour.BLACK);

            res += this.pieceActivityScore(state.board(), Colour.BLACK); //black's own pieces being active is good for black
            res -= this.pieceActivityScore(state.board(), Colour.WHITE);
        }

        //flip if we actually wanted to score for white
        return state.currentColour() == Colour.BLACK ? res : -res;
    }

    //higher number for a queen being in more danger
    private int queenSafetyScore(Board board, Colour queenColour) {
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
    private int pieceActivityScore(Board board, Colour colour) {
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

    private boolean isQueenSurrounded(@NonNull Board board, Colour colour) {
        Optional<HexCoordinate> queenAt = board.findPiece(new Piece(Bug.BEE, colour));
        if (queenAt.isEmpty()) return false;

        for (HexCoordinate neighbour : board.neighbours(queenAt.get())) {
            if (!board.isOccupied(neighbour)) return false;
        }
        return true;
    }

    //helper which plays out a move
    private @NonNull GameState fakeAdvance(@NonNull GameState oldState, @NonNull Move move) {
        Board board = oldState.board().copy();
        switch (move) {
            case PlaceMove mv -> board.addPiece(new Piece(mv.bug(), mv.colour()), mv.destination());
            case RelocateMove mv -> board.movePiece(mv.from(), mv.to());
        }
        Colour nextColor = oldState.currentColour() == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
        return new GameState(board, nextColor);
    }

    //helper which sorts moves based on score
    private @NonNull List<ScoredMove> sortMoves(Set<Move> moves, GameState state, boolean maximising) {
        List<ScoredMove> scored = moves.stream()
                .map(mv -> {
                    GameState nextState = this.fakeAdvance(state, mv);
                    return new ScoredMove(mv, nextState, this.score(nextState));
                })
                .sorted(Comparator.comparingInt(ScoredMove::score))
                .toList();
        return maximising ? scored.reversed() : scored;
    }
}