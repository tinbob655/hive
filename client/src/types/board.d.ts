export interface Board {
    cells: Cell[];
}

export interface Cell {
    coordinate: HexCoordinate;
    pieces: Piece[];
}

export interface HexCoordinate {
    q: number;
    r: number;
}

export interface Piece {
   Colour: Colour;
   Bug: Bug;
}

export type Colour = 'WHITE' | 'BLACK';
export type Bug = 'BEE' | 'BEETLE' | 'GRASSHOPPER' | 'SPIDER' | 'ANT' | 'LADYBUG' | 'MOSQUITO' | 'WOODLOUSE';