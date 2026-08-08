import React from 'react';
import {hexPoints} from "../../utils/hex.ts";
import {BUG_ICONS} from "./bugIcons/BugIcons.tsx";
import type {Bug, Colour} from "../../types/board";

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

    const Icon = bug ? BUG_ICONS[bug] : null;
    const iconSize = size * 0.85;

    return (
        <g className={classNames.join(' ')} onClick={onClick}>
            <polygon points={hexPoints(x, y, size)} />

            {Icon && (
                <svg
                    x={x - iconSize / 2}
                    y={y - iconSize / 2}
                    width={iconSize}
                    height={iconSize}
                    viewBox={"0 0 24 24"}
                    className={"hexTile__icon"}
                >
                    <Icon />
                </svg>
            )}

            {stackCount !== undefined && stackCount > 1 && (
                <g
                    className={"hexTile__stackBadge"}
                    transform={`translate(${x + size * 0.62}, ${y - size * 0.62})`}
                >
                    <circle r={size * 0.22} />
                    <text dy={"0.32em"} textAnchor={"middle"}>{stackCount}</text>
                </g>
            )}

            {isMoveTarget && !bug && (
                <circle cx={x} cy={y} r={size * 0.22} className={"hexTile__dot"} />
            )}
        </g>
    );
}