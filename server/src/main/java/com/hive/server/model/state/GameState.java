package com.hive.server.model.state;

import com.hive.server.model.board.Board;
import com.hive.server.model.enums.Colour;
import com.hive.server.model.move.Move;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

public record GameState(
        Board board,
        Colour currentColour
) {

    public @NonNull Set<@NonNull Move> legalMoves() {

        Set<Move> res = new HashSet<>();
        //TODO: IMPLEMENT ME

        return res;
    }

    public boolean isGameOver() {

        //TODO: IMPLEMENT ME
        return false;
    }
}
