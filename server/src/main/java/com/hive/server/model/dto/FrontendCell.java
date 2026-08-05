package com.hive.server.model.dto;

import com.hive.server.model.board.HexCoordinate;
import com.hive.server.model.board.Piece;

import java.util.List;

public record FrontendCell(
        HexCoordinate coordinate,
        List<Piece> pieces
) {
}
