import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function LadybugIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <circle cx="12" cy="6.3" r="2"/>
            <ellipse cx="12" cy="14.5" rx="6.2" ry="7.3"/>
            <path d="M12 8v13"/>
            <circle cx="9" cy="12" r="0.9" fill="currentColor"/>
            <circle cx="15" cy="12" r="0.9" fill="currentColor"/>
            <circle cx="9" cy="17" r="0.9" fill="currentColor"/>
            <circle cx="15" cy="17" r="0.9" fill="currentColor"/>
        </g>
    );
}