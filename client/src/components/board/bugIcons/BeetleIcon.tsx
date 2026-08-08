import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function BeetleIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <circle cx="12" cy="6" r="2"/>
            <path d="M12 8v2"/>
            <ellipse cx="12" cy="15" rx="6.2" ry="7"/>
            <path d="M12 8v13"/>
            <path d="M6.2 12h1.8M6 15h2M6.2 18h1.8"/>
            <path d="M16 12h1.8M16 15h2M16 18h1.8"/>
        </g>
    );
}