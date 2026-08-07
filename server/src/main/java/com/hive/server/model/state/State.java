package com.hive.server.model.state;

import com.hive.server.model.board.Board;
import com.hive.server.model.enums.Colour;
import com.hive.server.model.move.Move;
import lombok.NonNull;

import java.util.Set;

public interface State {

    Board board();
    Colour currentColour();

    Set<@NonNull Move> legalMoves();
    boolean isGameOver();
}
