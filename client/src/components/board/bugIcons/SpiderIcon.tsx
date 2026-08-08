import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function SpiderIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <circle cx="12" cy="12" r="3.4"/>
            <path d="M9 10L3 6M9 12H2M9 14L3 18"/>
            <path d="M15 10l6-4M15 12h7M15 14l6 4"/>
            <path d="M10 9l-1-4M14 9l1-4"/>
        </g>
    );
}