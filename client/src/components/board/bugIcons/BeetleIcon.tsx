import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function BeetleIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <ellipse cx="12" cy="4.8" rx="2.2" ry="1.8" />
            <path d="M10.5 3.4c-.7-.6-1.4-.7-2-.4M13.5 3.4c.7-.6 1.4-.7 2-.4" />
            <path d="M8.4 7.6c0-1.6 1.6-2.6 3.6-2.6s3.6 1 3.6 2.6-1.6 2.4-3.6 2.4-3.6-.8-3.6-2.4Z" />
            <path d="M6.6 12.5c0-2.4 2.4-3.5 5.4-3.5s5.4 1.1 5.4 3.5v3.5c0 3.4-2.4 5.8-5.4 5.8s-5.4-2.4-5.4-5.8v-3.5Z" />
            <path d="M12 9.4v12.4" />
            <path d="M6.9 14.2h10.2M7.1 17h9.8M7.8 19.6h8.4" />
            <path d="M6.6 11l-3.4-1.6M6.4 14h-3.6M6.6 17l-3.4 1.6" />
            <path d="M17.4 11l3.4-1.6M17.6 14h3.6M17.4 17l3.4 1.6" />
        </g>
    );
}