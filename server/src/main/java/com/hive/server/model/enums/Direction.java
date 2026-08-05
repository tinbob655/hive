package com.hive.server.model.enums;

public enum Direction {
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(0, -1);

    public final int dq;
    public final int dr;

    Direction(int dq, int dr) {
        this.dq = dq;
        this.dr = dr;
    }
}
