package com.hive.server.model.board;

import com.hive.server.model.enums.Direction;
import org.jspecify.annotations.NonNull;

public record HexCoordinate(int q, int r) {

    public @NonNull HexCoordinate neighbour(@NonNull Direction direction) {
        return new HexCoordinate(
                this.q + direction.dq,
                this.r + direction.dr
        );
    }
}
