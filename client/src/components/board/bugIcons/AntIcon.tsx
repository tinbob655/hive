import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function AntIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <circle cx="12" cy="5.4" r="2.1" />
            <path d="M10.6 3.8c-.8-1-1.6-1.2-2.4-1M13.4 3.8c.8-1 1.6-1.2 2.4-1" />
            <ellipse cx="12" cy="10.6" rx="1.9" ry="2.2" />
            <path d="M12 12.6v1.4" />
            <ellipse cx="12" cy="17.8" rx="3.6" ry="4.6" />
            <path d="M10.5 9.4L5 7.6M10.7 11L4.4 10.6M11 12.8L5.6 14" />
            <path d="M13.5 9.4L19 7.6M13.3 11L19.6 10.6M13 12.8L18.4 14" />
        </g>
    );
}