import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function BeeIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <circle cx="12" cy="5.2" r="1.8" />
            <path d="M10.8 3.8c-.6-1-1.3-1.4-2.1-1.3M13.2 3.8c.6-1 1.3-1.4 2.1-1.3" />
            <ellipse cx="7.5" cy="10" rx="3.6" ry="2.4" transform="rotate(-25 7.5 10)" />
            <ellipse cx="16.5" cy="10" rx="3.6" ry="2.4" transform="rotate(25 16.5 10)" />
            <path d="M8 12.5c0-2 1.8-3.2 4-3.2s4 1.2 4 3.2v3.8c0 3-1.8 5.2-4 5.2s-4-2.2-4-5.2v-3.8Z" />
            <path d="M8.1 13.8h7.8M8.2 16.4h7.6M8.6 18.8h6.8" />
            <path d="M12 21.5v1.3" />
            <path d="M8.3 15l-2.6 1.4M8.3 17l-2.3 2M15.7 15l2.6 1.4M15.7 17l2.3 2" />
        </g>
    );
}