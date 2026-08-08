import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function GrasshopperIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <ellipse cx="10" cy="12" rx="5" ry="3" transform="rotate(-10 10 12)"/>
            <circle cx="16.3" cy="9.3" r="2"/>
            <path d="M9 15c1 3 3 5 3 8"/>
            <path d="M12 23l3-3"/>
            <path d="M6 11l-3-1"/>
            <path d="M6.3 13.8l-3 1.2"/>
            <path d="M18 8.2c1-1 2-1 3-.5"/>
        </g>
    );
}