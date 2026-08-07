import React from 'react';
import {hexPoints} from "../../utils/hex.ts";
import type {Bug, Colour} from "../../types/board";

const BUG_LABELS: Record<Bug, string> = {
    BEE: 'Q',
    BEETLE: 'B',
    GRASSHOPPER: 'G',
    SPIDER: 'S',
    ANT: 'A',
    LADYBUG: 'L',
    MOSQUITO: 'M',
    WOODLOUSE: 'W',
};
//TODO: GET BETTER ICONS HERE ^

interface Props {
    x: number;
    y: number;
    size: number;
    bug?: Bug;
    colour?: Colour;
    stackCount?: number;
    isMoveTarget?: boolean;
    isSelected?: boolean;
    isClickable?: boolean;
    onClick?: () => void;
}

export default function HexTile({
                                    x, y, size, bug, colour, stackCount, isMoveTarget, isSelected, isClickable, onClick,
                                }: Props): React.ReactElement {

    const classNames: string[] = ['hexTile'];
    if (colour) classNames.push(colour === 'WHITE' ? 'hexTile--white' : 'hexTile--black');
    if (isMoveTarget) classNames.push('hexTile--moveTarget');
    if (isSelected) classNames.push('hexTile--selected');
    if (isClickable) classNames.push('hexTile--clickable');

    return (
        <g className={classNames.join(' ')} onClick={onClick}>
            <polygon points={hexPoints(x, y, size)} />

            {bug && (
                <text x={x} y={y} dy={"0.35em"} textAnchor={"middle"}>
                {BUG_LABELS[bug]}
                </text>
            )}

            {stackCount !== undefined && stackCount > 1 && (
                <text
                    x={x + size * 0.55}
                    y={y - size * 0.55}
                    className={"hexTile__stack"}
                    textAnchor={"middle"}
                >
                    x{stackCount}
                </text>
            )}

            {isMoveTarget && !bug && (
                <circle cx={x} cy={y} r={size * 0.22} className={"hexTile__dot"} />
            )}
    </g>
);
}