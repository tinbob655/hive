package com.hive.server.model.state;

import com.hive.server.model.board.Board;
import com.hive.server.model.board.HexCoordinate;
import com.hive.server.model.board.Piece;
import com.hive.server.model.enums.Bug;
import com.hive.server.model.enums.Colour;
import com.hive.server.model.enums.Direction;
import com.hive.server.model.move.Move;
import com.hive.server.model.move.PlaceMove;
import com.hive.server.model.move.RelocateMove;
import lombok.NonNull;

import java.util.*;

public final class GameState implements State {

    private static final Map<Bug, Integer> STARTING_COUNTS = Map.of(
            Bug.BEE, 1,
            Bug.BEETLE, 2,
            Bug.GRASSHOPPER, 3,
            Bug.SPIDER, 2,
            Bug.ANT, 3,
            Bug.LADYBUG, 1,
            Bug.MOSQUITO, 1,
            Bug.WOODLOUSE, 1
    );

    private Set<Move> cachedLegalMoves = null;
    private Boolean cachedGameOver = null;

    private final Board board;
    private final Colour currentColour;


    public GameState(Board board, Colour currentColour) {
        this.board = board;
        this.currentColour = currentColour;
    }

    @Override public Board board() {
        return this.board;
    }
    @Override public Colour currentColour() {
        return this.currentColour;
    }

    @Override
    public @NonNull Set<@NonNull Move> legalMoves() {

        if (this.cachedLegalMoves != null && !this.cachedLegalMoves.isEmpty()) return this.cachedLegalMoves;

        Set<Move> res = new HashSet<>();
        res.addAll(this.placementMoves());
        res.addAll(this.relocationMoves());
        res.addAll(this.woodlouseMoves());

        //save legal moves for later
        this.cachedLegalMoves = new HashSet<>(res);
        return res;
    }

    @Override
    public boolean isGameOver() {

        if (this.cachedGameOver != null) return this.cachedGameOver;

        boolean over = this.isQueenSurrounded(Colour.WHITE) || this.isQueenSurrounded(Colour.BLACK);
        this.cachedGameOver = over;
        return over;
    }

    private boolean isQueenSurrounded(Colour colour) {
        Optional<HexCoordinate> queenAt = this.board.findPiece(new Piece(Bug.BEE, colour));
        if (queenAt.isEmpty()) return false;

        for (HexCoordinate neighbour : this.board.neighbours(queenAt.get())) {
            if (!this.board.isOccupied(neighbour)) return false;
        }
        return true;
    }

    private Set<PlaceMove> placementMoves() {
        Set<PlaceMove> moves = new HashSet<>();

        int piecesPlaced = this.piecesPlacedBy(this.currentColour);
        boolean queenPlaced = this.board.findPiece(new Piece(Bug.BEE, this.currentColour)).isPresent();
        boolean mustPlaceQueen = !queenPlaced && piecesPlaced == 3; //queen forced down by 4th placement

        Set<HexCoordinate> destinations = this.validPlacementCells();

        for (Bug bug : Bug.values()) {
            if (mustPlaceQueen && bug != Bug.BEE) continue;
            if (this.remainingInBank(this.currentColour, bug) <= 0) continue;

            for (HexCoordinate dest : destinations) {
                moves.add(new PlaceMove(this.currentColour, bug, dest));
            }
        }

        return moves;
    }

    private Set<HexCoordinate> validPlacementCells() {
        Set<HexCoordinate> occupied = this.board.occupiedCoordinates();

        if (occupied.isEmpty()) {
            return Set.of(new HexCoordinate(0, 0));
        }

        if (occupied.size() == 1) {
            return this.board.neighbours(occupied.iterator().next());
        }

        Set<HexCoordinate> candidates = new HashSet<>();
        for (HexCoordinate coord : occupied) {
            if (Objects.requireNonNull(this.board.getTopPiece(coord)).colour() != this.currentColour) continue;
            for (HexCoordinate neighbour : this.board.neighbours(coord)) {
                if (!this.board.isOccupied(neighbour)) candidates.add(neighbour);
            }
        }

        //can't place touching the opponent
        candidates.removeIf(candidate -> this.board.neighbours(candidate).stream()
                .anyMatch(n -> this.board.isOccupied(n)
                        && Objects.requireNonNull(this.board.getTopPiece(n)).colour() != this.currentColour));

        return candidates;
    }

    private int piecesPlacedBy(Colour colour) {
        int total = 0;
        for (Bug bug : Bug.values()) {
            total += STARTING_COUNTS.get(bug) - this.remainingInBank(colour, bug);
        }
        return total;
    }

    private int remainingInBank(Colour colour, Bug bug) {
        int placed = 0;
        for (Deque<Piece> stack : this.board.getCells().values()) {
            for (Piece piece : stack) {
                if (piece.colour() == colour && piece.bug() == bug) placed++;
            }
        }
        return STARTING_COUNTS.get(bug) - placed;
    }
    // --- relocation ---

    private Set<RelocateMove> relocationMoves() {
        Set<RelocateMove> moves = new HashSet<>();

        if (this.board.findPiece(new Piece(Bug.BEE, this.currentColour)).isEmpty()) return moves; //can't move pieces before your queen is down

        for (HexCoordinate coord : this.board.occupiedCoordinates()) {
            Piece top = this.board.getTopPiece(coord);
            assert top != null;
            if (top.colour() != this.currentColour) continue;
            if (this.board.willBreakHive(coord)) continue; //one-hive rule

            for (HexCoordinate dest : this.destinationsFor(top.bug(), coord)) {
                moves.add(new RelocateMove(this.currentColour, coord, dest));
            }
        }

        return moves;
    }

    private Set<HexCoordinate> destinationsFor(Bug bug, HexCoordinate from) {
        return switch (bug) {
            case BEE, WOODLOUSE -> this.oneStepSlides(from);
            case BEETLE -> this.board.neighbours(from);
            case GRASSHOPPER -> this.grasshopperJumps(from);
            case SPIDER -> this.slideExactly(from, 3);
            case ANT -> this.slideAnyDistance(from);
            case LADYBUG -> this.ladybugMoves(from);
            case MOSQUITO -> this.mosquitoMoves(from);
        };
    }

    private Set<HexCoordinate> oneStepSlides(HexCoordinate from) {
        Set<HexCoordinate> res = new HashSet<>();
        for (HexCoordinate to : this.board.neighbours(from)) {
            if (this.board.canSlide(from, to)) res.add(to);
        }
        return res;
    }

    private Set<HexCoordinate> grasshopperJumps(HexCoordinate from) {
        Set<HexCoordinate> res = new HashSet<>();

        for (Direction direction : Direction.values()) {
            HexCoordinate probe = from.neighbour(direction);
            if (!this.board.isOccupied(probe)) continue;

            while (this.board.isOccupied(probe)) {
                probe = probe.neighbour(direction);
            }
            res.add(probe);
        }

        return res;
    }

    private Set<HexCoordinate> slideExactly(HexCoordinate from, int steps) {
        Set<HexCoordinate> current = Set.of(from);
        Set<HexCoordinate> visited = new HashSet<>(Set.of(from));

        for (int i = 0; i < steps; i++) {
            Set<HexCoordinate> next = new HashSet<>();
            for (HexCoordinate coord : current) {
                for (HexCoordinate candidate : this.board.neighbours(coord)) {
                    if (visited.contains(candidate)) continue;
                    if (!this.board.canSlide(coord, candidate)) continue;
                    next.add(candidate);
                }
            }
            current = next;
            visited.addAll(next);
        }

        return current;
    }

    private Set<HexCoordinate> slideAnyDistance(HexCoordinate from) {
        Set<HexCoordinate> visited = new HashSet<>(Set.of(from));
        Deque<HexCoordinate> toVisit = new ArrayDeque<>(Set.of(from));

        while (!toVisit.isEmpty()) {
            HexCoordinate coord = toVisit.poll();
            for (HexCoordinate candidate : this.board.neighbours(coord)) {
                if (visited.contains(candidate)) continue;
                if (!this.board.canSlide(coord, candidate)) continue;
                visited.add(candidate);
                toVisit.add(candidate);
            }
        }

        visited.remove(from);
        return visited;
    }

    private Set<HexCoordinate> ladybugMoves(HexCoordinate from) {
        Set<HexCoordinate> res = new HashSet<>();

        for (HexCoordinate first : this.board.neighbours(from)) {
            if (!this.board.isOccupied(first)) continue;

            for (HexCoordinate second : this.board.neighbours(first)) {
                if (second.equals(from) || !this.board.isOccupied(second)) continue;

                for (HexCoordinate third : this.board.neighbours(second)) {
                    if (!third.equals(from) && !this.board.isOccupied(third)) res.add(third);
                }
            }
        }

        return res;
    }

    private Set<HexCoordinate> mosquitoMoves(HexCoordinate from) {

        if (this.board.stackHeight(from) > 1) {
            return this.board.neighbours(from); //mosquito on top of the hive can only copy the Beetle
        }

        Set<Bug> touchingBugs = new HashSet<>();
        for (HexCoordinate neighbour : this.board.neighbours(from)) {
            Piece piece = this.board.getTopPiece(neighbour);
            if (piece != null && piece.bug() != Bug.MOSQUITO) touchingBugs.add(piece.bug());
        }

        Set<HexCoordinate> res = new HashSet<>();
        for (Bug bug : touchingBugs) {
            res.addAll(this.destinationsFor(bug, from));
        }
        return res;
    }

    // --- woodlouse ---
    private @NonNull Set<@NonNull RelocateMove> woodlouseMoves() {
        Set<RelocateMove> res = new HashSet<>();

        //if there's no woodlouse or no queen don't do anything
        Optional<HexCoordinate> maybeWoodlousePosition = this.board.findPiece(new Piece(Bug.WOODLOUSE, this.currentColour));
        boolean isQueenPresent = this.board.findPiece(new Piece(Bug.BEE, this.currentColour)).isPresent();
        if (maybeWoodlousePosition.isEmpty() || !isQueenPresent) return res;

        HexCoordinate woodlousePosition = maybeWoodlousePosition.get();

        //need to classify all neighbours of the woodlouse
        Set<HexCoordinate> emptyNeighbours = new HashSet<>();
        Set<HexCoordinate> occupiedNeighbours = new HashSet<>();
        for (HexCoordinate neighbour : this.board.neighbours(woodlousePosition)) {

            //if the neighbour is blank
            if (!this.board.isOccupied(neighbour)) {
                emptyNeighbours.add(neighbour);
                continue;
            }

            //can never move a stack of bugs
            if (this.board.stackHeight(neighbour) > 1) continue;

            //make sure moving the bug does not break the hive
            if (this.board.willBreakHive(neighbour)) continue;

            occupiedNeighbours.add(neighbour);
        }

        //now generate the moves
        for (HexCoordinate occupiedNeighbour : occupiedNeighbours) {
            for (HexCoordinate emptyNeighbour : emptyNeighbours) {
                res.add(new RelocateMove(this.currentColour, occupiedNeighbour, emptyNeighbour));
            }
        }
        return res;
    }
}