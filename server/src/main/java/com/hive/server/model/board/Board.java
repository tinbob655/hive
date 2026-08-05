package com.hive.server.model.board;

import com.hive.server.model.dto.FrontendBoard;
import com.hive.server.model.dto.FrontendCell;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

public final class Board {

    @Getter
    private final Map<HexCoordinate, Deque<Piece>> cells = new HashMap<>();

    public Board() {}
    public Board(Map<HexCoordinate, Deque<Piece>> cells) {
        this.cells.putAll(cells);
    }

    public void addPiece(Piece pieceToAdd, HexCoordinate whereToAdd) {

        Deque<Piece> stack = this.cells.computeIfAbsent(whereToAdd, k -> new ArrayDeque<>());

        stack.addFirst(pieceToAdd);
    }

    public void movePiece(HexCoordinate oldLocation, HexCoordinate newLocation) {

        Deque<Piece> oldStack = this.cells.get(oldLocation);

        Piece piece = oldStack.pollFirst();

        if (oldStack.isEmpty()) {
            this.cells.remove(oldLocation);
        }

        this.cells.computeIfAbsent(newLocation, k -> new ArrayDeque<>())
                .addFirst(piece);
    }

    public @NonNull FrontendBoard getFrontendBoard() {

        List<FrontendCell> frontendCells = new LinkedList<>();
        for (Map.Entry<HexCoordinate, Deque<Piece>> cell : this.cells.entrySet()) {
            frontendCells.add(new FrontendCell(cell.getKey(), List.copyOf(cell.getValue())));
        }

        return new FrontendBoard(frontendCells);
    }

    public Board copy() {
        return new Board(Map.copyOf(this.cells));
    }
}
