package com.hive.server.model.state;

import com.hive.server.model.board.Board;
import com.hive.server.model.enums.Colour;

public record GameState(
        Board board,
        Colour currentColour
) {
}
