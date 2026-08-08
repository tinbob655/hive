import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function MosquitoIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <ellipse cx="12" cy="12.5" rx="2.3" ry="4"/>
            <path d="M12 8.5c0-3-.5-4.5-1.5-6"/>
            <ellipse cx="8" cy="9.3" rx="2.8" ry="1.5" transform="rotate(-25 8 9.3)"/>
            <ellipse cx="16" cy="9.3" rx="2.8" ry="1.5" transform="rotate(25 16 9.3)"/>
            <path d="M10 14.5l-4 2M10 16.5l-4 4M10 12.5l-5-1"/>
            <path d="M14 14.5l4 2M14 16.5l4 4M14 12.5l5-1"/>
        </g>
    );
}