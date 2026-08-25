import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function SpiderIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <ellipse cx="12" cy="9.4" rx="2.6" ry="2.2" />
            <ellipse cx="12" cy="15.4" rx="4.2" ry="5" />
            <path d="M9.8 8.6L4.6 6.4M4.6 6.4L2 7.4" />
            <path d="M9.6 10.6L3.6 10.2M3.6 10.2L1 10.6" />
            <path d="M10 12.8L4.4 14M4.4 14L2 15.6" />
            <path d="M10.6 15L5.2 17.6M5.2 17.6L3.2 20" />
            <path d="M14.2 8.6L19.4 6.4M19.4 6.4L22 7.4" />
            <path d="M14.4 10.6L20.4 10.2M20.4 10.2L23 10.6" />
            <path d="M14 12.8L19.6 14M19.6 14L22 15.6" />
            <path d="M13.4 15L18.8 17.6M18.8 17.6L20.8 20" />
        </g>
    );
}