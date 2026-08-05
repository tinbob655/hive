package com.hive.server.model.move;

import com.hive.server.model.board.HexCoordinate;
import com.hive.server.model.enums.Bug;
import com.hive.server.model.enums.Colour;

public record PlaceMove(
        Colour colour,
        Bug bug,
        HexCoordinate destination
) implements Move {
}
