package com.hive.server.model.move;

import com.hive.server.model.enums.Colour;

public sealed interface Move permits PlaceMove, RelocateMove {
    Colour colour();
}
