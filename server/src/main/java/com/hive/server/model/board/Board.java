package com.hive.server.model.board;

import com.hive.server.model.dto.FrontendBoard;
import com.hive.server.model.dto.FrontendCell;
import com.hive.server.model.enums.Direction;
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

    public @NonNull Set<HexCoordinate> neighbours(@NonNull HexCoordinate coord) {
        Set<HexCoordinate> res = new HashSet<>();
        for (Direction direction : Direction.values()) {
            res.add(coord.neighbour(direction));
        }
        return res;
    }

    public boolean isOccupied(HexCoordinate coord) {
        Deque<Piece> stack = this.cells.get(coord);
        return stack != null && !stack.isEmpty();
    }

    public Piece getTopPiece(HexCoordinate coord) {
        Deque<Piece> stack = this.cells.get(coord);
        return (stack == null || stack.isEmpty()) ? null : stack.peekFirst();
    }

    public int stackHeight(HexCoordinate coord) {
        Deque<Piece> stack = this.cells.get(coord);
        return stack == null ? 0 : stack.size();
    }

    public Set<HexCoordinate> occupiedCoordinates() {
        Set<HexCoordinate> res = new HashSet<>();
        for (Map.Entry<HexCoordinate, Deque<Piece>> cell : this.cells.entrySet()) {
            if (!cell.getValue().isEmpty()) {
                res.add(cell.getKey());
            }
        }
        return res;
    }

    //true if we are safe to remove the top piece without breaking the hive
    public boolean isConnectedWithoutTop(HexCoordinate ignore) {
        Set<HexCoordinate> occupied = new HashSet<>(this.occupiedCoordinates());

        //can always remove a stacked piece
        if (this.stackHeight(ignore) > 1) return true;

        occupied.remove(ignore);
        if (occupied.isEmpty()) return true;

        //do a traversal
        Deque<HexCoordinate> toVisit = new ArrayDeque<>();
        Set<HexCoordinate> visited = new HashSet<>();
        HexCoordinate start = occupied.iterator().next();
        toVisit.add(start);
        visited.add(start);

        while (!toVisit.isEmpty()) {
            HexCoordinate current = toVisit.poll();
            for (HexCoordinate neighbour : this.neighbours(current)) {
                if (occupied.contains(neighbour) && visited.add(neighbour)) {
                    toVisit.add(neighbour);
                }
            }
        }

        //if we manage to visit every cell then we have avoided breaking the hive
        return visited.size() == occupied.size();
    }

    //pieces can only slide into a cell if there is space
    public boolean canSlide(HexCoordinate from, HexCoordinate to) {
        if (this.isOccupied(to)) return false;

        List<HexCoordinate> shared = this.neighbours(from).stream()
                .filter(this.neighbours(to)::contains)
                .toList();

        return !(this.isOccupied(shared.get(0)) && this.isOccupied(shared.get(1)));
    }
}
