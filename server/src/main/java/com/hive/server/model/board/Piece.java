package com.hive.server.model.board;

import com.hive.server.model.enums.Bug;
import com.hive.server.model.enums.Colour;

public record Piece(
        Bug bug,
        Colour colour
) {
}
