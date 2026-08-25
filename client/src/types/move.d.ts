import type {Bug, Colour, HexCoordinate} from './board';

export interface Move {
    colour: Colour;
}

export interface PlaceMove extends Move {
    bug: Bug;
    destination: HexCoordinate;
}

export interface RelocateMove extends Move {
    from: HexCoordinate;
    to: HexCoordinate;
}

export type SkipMove = Move;