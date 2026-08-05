import type {HexCoordinate} from "../types/board";

export const HEX_SIZE = 40;

/**
 * Converts an axial (q, r) coordinate into pixel coordinates for a
 * "pointy-top" hexagon layout
 */
export function axialToPixel(coordinate: HexCoordinate, size: number = HEX_SIZE): { x: number; y: number } {
    const x = size * (Math.sqrt(3) * coordinate.q + (Math.sqrt(3) / 2) * coordinate.r);
    const y = size * (1.5 * coordinate.r);
    return {x, y};
}

/**
 * Builds the "points" attribute for an SVG <polygon> describing a
 * pointy-top hexagon centred at (centreX, centreY). Corners sit at
 * 30, 90, 150... degrees for a pointy-top shape.
 */
export function hexPoints(centreX: number, centreY: number, size: number = HEX_SIZE): string {
    const corners: string[] = [];
    for (let i = 0; i < 6; i++) {
        const angle = (Math.PI / 180) * (60 * i - 30);
        const x = centreX + size * Math.cos(angle);
        const y = centreY + size * Math.sin(angle);
        corners.push(`${x.toFixed(2)},${y.toFixed(2)}`);
    }
    return corners.join(' ');
}

/** Turns a coordinate into a stable string key, for use in Maps/Sets and React keys. */
export function coordKey(coordinate: HexCoordinate): string {
    return `${coordinate.q},${coordinate.r}`;
}