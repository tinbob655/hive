import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function AntIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <circle cx="12" cy="6" r="2"/>
            <circle cx="12" cy="11" r="2.1"/>
            <ellipse cx="12" cy="17" rx="3.4" ry="4.3"/>
            <path d="M10.5 4.5c-.8-1-1.5-1.2-2.3-1M13.5 4.5c.8-1 1.5-1.2 2.3-1"/>
            <path d="M9.6 10l-4-2M9 11.5H4M9.6 13l-4 2"/>
            <path d="M14.4 10l4-2M15 11.5h5M14.4 13l4 2"/>
        </g>
    );
}