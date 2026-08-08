import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function BeeIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <ellipse cx="8" cy="8" rx="4" ry="3" transform="rotate(-20 8 8)"/>
            <ellipse cx="16" cy="8" rx="4" ry="3" transform="rotate(20 16 8)"/>
            <ellipse cx="12" cy="14.5" rx="5" ry="6.5"/>
            <path d="M7.3 12.5h9.4"/>
            <path d="M7.6 16h8.8"/>
            <path d="M8.3 19.5h7.4"/>
            <path d="M12 21v2"/>
            <path d="M10 8.5c-.5-1.5-1.5-2-2.5-2"/>
            <path d="M14 8.5c.5-1.5 1.5-2 2.5-2"/>
        </g>
    );
}