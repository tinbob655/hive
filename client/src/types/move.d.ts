import type {Bug, Colour, HexCoordinate} from './board';

export interface PlaceMove {
    colour: Colour;
    bug: Bug;
    destination: HexCoordinate;
}

export interface RelocateMove {
    colour: Colour;
    from: HexCoordinate;
    to: HexCoordinate;
}

export type Move = PlaceMove | RelocateMove;