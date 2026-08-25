import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function GrasshopperIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <ellipse cx="16.5" cy="7" rx="2.2" ry="1.8" transform="rotate(30 16.5 7)" />
            <circle cx="17.4" cy="6.2" r="0.5" fill="currentColor" />
            <path d="M18.3 5.4c1-.6 1.8-.4 2.4.2" />
            <path d="M6 15c0-2.4 4.2-4.6 9-4.6 1.6 0 2.6.6 2.6 1.8 0 1.6-1.8 2.4-4.4 2.4-3.4 0-7.2 1-7.2 3.4 0 1.6 1.6 2.6 3.6 2.6" />
            <path d="M13 12l3-3M13 13.4l3.6-1" />
            <path d="M8.4 15.6c-.6 1.6-.4 3 .8 4" />
            <path d="M9.2 19.6c-1.4.5-2.4 1.7-2.8 3.2" />
        </g>
    );
}