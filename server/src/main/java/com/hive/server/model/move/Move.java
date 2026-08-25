package com.hive.server.model.move;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.hive.server.model.enums.Colour;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
public sealed interface Move permits PlaceMove, RelocateMove, SkipMove {
    Colour colour();
}
