import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function WoodlouseIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <path d="M4.5 13.5a7.5 7 0 0 1 15 0" />
            <path d="M5.4 13.5a6.6 6.1 0 0 1 13.2 0" />
            <path d="M6.4 13.7a5.6 5.2 0 0 1 11.2 0" />
            <path d="M7.4 14a4.6 4.2 0 0 1 9.2 0" />
            <path d="M8.4 14.3a3.6 3.2 0 0 1 7.2 0" />
            <path d="M4 13.6c-.6.4-1 1-1 1.7" />
            <path d="M20 13.6c.6.4 1 1 1 1.7" />
            <path d="M6 16.4l-1.6 1.4M9 17l-1 1.8M12 17.2v2M15 17l1 1.8M18 16.4l1.6 1.4" />
        </g>
    );
}