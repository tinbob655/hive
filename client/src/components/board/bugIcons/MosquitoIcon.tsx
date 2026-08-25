import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function MosquitoIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <circle cx="12" cy="6.4" r="1.4" />
            <path d="M12 5c0-2 .3-3.4 1-4.6" />
            <path d="M11.3 7.6c-.6 2-1 4-1 6.4 0 3.6.6 6.4 1.7 8.6.5-2.2 1-5 1-8.6 0-2.4-.3-4.4-.9-6.4-.3-.6-1.5-.6-1.8 0Z" />
            <ellipse cx="7.4" cy="9.6" rx="3.2" ry="1.6" transform="rotate(-20 7.4 9.6)" />
            <ellipse cx="16.6" cy="9.6" rx="3.2" ry="1.6" transform="rotate(20 16.6 9.6)" />
            <path d="M10.6 13l-5 2.4M10.6 15.4l-5.4 5M10.8 17.6l-4.6 6.2" />
            <path d="M13.4 13l5 2.4M13.4 15.4l5.4 5M13.2 17.6l4.6 6.2" />
        </g>
    );
}