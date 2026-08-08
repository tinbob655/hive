import React from "react";
import {baseIcon, type IconProps} from "./BugIcons.tsx";

export default function WoodlouseIcon(props: IconProps): React.ReactElement {
    return (
        <g {...baseIcon} {...props}>
            <path d="M6 12a6 8 0 0 1 12 0"/>
            <path d="M5.6 12v1.5a6.4 6 0 0 0 12.8 0V12"/>
            <path d="M6.1 12h11.8"/>
            <path d="M6.5 15h11"/>
            <path d="M7.3 17.5h9.4"/>
            <path d="M8.6 19.6h6.8"/>
        </g>
    );
}