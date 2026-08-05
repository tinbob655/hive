package com.hive.server.service.bot;

import com.hive.server.model.board.Board;
import com.hive.server.model.board.Piece;
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

    @Override
    public @NonNull Move decideMove(@NonNull GameState state) {
        Set<Move> legalMoves = state.legalMoves();
        List<Move> sortedMoves = this.sortMoves(legalMoves, state, true);

        Move bestMove = sortedMoves.getFirst();
        int bestScore = Integer.MIN_VALUE;

        for (Move move : sortedMoves) {
            GameState nextState = this.fakeAdvance(state, move);

            int eval = this.minimax(nextState, SEARCH_DEPTH -1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if (eval > bestScore) {
                bestScore = eval;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(GameState state, int depth, int alpha, int beta, boolean maximising) {

        if (depth == 0 || state.isGameOver()) {
            return score(state);
        }

        Set<Move> legalMoves = state.legalMoves();
        List<Move> sortedMoves = this.sortMoves(legalMoves, state, maximising);

        if (maximising) {

            int maxScore = Integer.MIN_VALUE;
            for (Move move : sortedMoves) {
                GameState afterMove = this.fakeAdvance(state, move);

                int eval = this.minimax(afterMove, depth - 1, alpha, beta, false);
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
            for (Move move : sortedMoves) {
                GameState afterMove = this.fakeAdvance(state, move);

                int eval = this.minimax(afterMove, depth - 1, alpha, beta, true);
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
        int res = 0;

        //TODO: WORK THESE OUT
        int QUEEN_NEIGHBOURS_WEIGHT = 7;

        int enemyQueenNeighbours = -1;
        res += (enemyQueenNeighbours * QUEEN_NEIGHBOURS_WEIGHT);

        int ownQueenNeighbours = -1;
        res -= (ownQueenNeighbours * QUEEN_NEIGHBOURS_WEIGHT);

        return state.currentColour() == Colour.BLACK ? res : -res;
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
    private @NonNull List<Move> sortMoves(Set<Move> moves, GameState state, boolean maximising) {
        List<Move> sorted = moves.stream()
                .sorted(Comparator.comparingInt(mv -> {
                    GameState nextState = this.fakeAdvance(state, mv);
                    return this.score(nextState);
                }))
                .toList();

        //need to do highest score first if maximising
        return maximising ? sorted.reversed() : sorted;
    }
}
