import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function LadybugIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <circle cx="12" cy="5.6" r="2" />
            <path d="M10.8 4c-.6-.8-1.2-1-1.8-.8M13.2 4c.6-.8 1.2-1 1.8-.8" />
            <path d="M5.8 14c0-4 2.8-6.4 6.2-6.4s6.2 2.4 6.2 6.4-2.8 7.4-6.2 7.4-6.2-3.4-6.2-7.4Z" />
            <path d="M12 8v13" />
            <circle cx="9" cy="12" r="0.9" fill="currentColor" />
            <circle cx="15" cy="12" r="0.9" fill="currentColor" />
            <circle cx="8.3" cy="16.2" r="0.9" fill="currentColor" />
            <circle cx="15.7" cy="16.2" r="0.9" fill="currentColor" />
            <circle cx="9.6" cy="19.2" r="0.8" fill="currentColor" />
            <circle cx="14.4" cy="19.2" r="0.8" fill="currentColor" />
            <path d="M6.2 12.6l-3-1M5.9 15.6h-3M6.4 18.6l-2.8 1.4" />
            <path d="M17.8 12.6l3-1M18.1 15.6h3M17.6 18.6l2.8 1.4" />
        </g>
    );
}