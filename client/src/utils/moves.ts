import type {Board, Bug, Colour, HexCoordinate} from "../types/board";
import type {Move, PlaceMove} from "../types/move";

export const STARTING_BUG_COUNTS: Record<Bug, number> = {
    BEE: 1,
    BEETLE: 2,
    GRASSHOPPER: 3,
    SPIDER: 2,
    ANT: 3,
    LADYBUG: 1,
    MOSQUITO: 1,
    WOODLOUSE: 1,
};

export interface BankEntry {
    bug: Bug;
    count: number;
}

//works out which pieces a player has left to place
export function getBank(board: Board, colour: Colour): BankEntry[] {

    const placed: Partial<Record<Bug, number>> = {};

    for (const cell of board.cells) {
        for (const piece of cell.pieces) {
            if (piece.colour === colour) {
                placed[piece.bug] = (placed[piece.bug] ?? 0) + 1;
            }
        }
    }

    return (Object.keys(STARTING_BUG_COUNTS) as Bug[])
        .map((bug) => ({bug, count: STARTING_BUG_COUNTS[bug] - (placed[bug] ?? 0)}))
        .filter((entry) => entry.count > 0);
}

/** PlaceMove and RelocateMove are told apart just by shape: a PlaceMove has `destination`, a RelocateMove has `from`/`to`. */
export function isPlaceMove(move: Move): move is PlaceMove {
    return 'destination' in move;
}

export function moveDestination(move: Move): HexCoordinate {
    return isPlaceMove(move) ? move.destination : move.to;
}