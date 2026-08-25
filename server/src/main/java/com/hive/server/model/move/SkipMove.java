package com.hive.server.model.move;

import com.hive.server.model.enums.Colour;

public record SkipMove(
        Colour colour
) implements Move {
    @Override
    public Colour colour() {
        return this.colour;
    }
}
