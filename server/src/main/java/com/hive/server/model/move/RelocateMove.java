package com.hive.server.model.move;

import com.hive.server.model.board.HexCoordinate;
import com.hive.server.model.enums.Colour;

public record RelocateMove(
        Colour colour,
        HexCoordinate from,
        HexCoordinate to
) implements Move {
}
