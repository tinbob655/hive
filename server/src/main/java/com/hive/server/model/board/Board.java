package com.hive.server.model.board;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public final class Board {

    private final Map<HexCoordinate, Deque<Piece>> cells = new HashMap<>();
}
