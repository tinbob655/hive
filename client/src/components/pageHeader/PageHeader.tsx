import React from 'react';
import './pageHeader.scss';

interface Params {
    title: string;
    subtitle: string;
}

export default function PageHeader({title, subtitle}: Params): React.ReactElement {

    return (
        <div className={"pageHeader"}>
            <h1 className={"alignLeft"}>
                {title}
            </h1>
            <p className={"alignLeft"}>
                {subtitle}
            </p>

            <div className={"sectionDivider"} />
        </div>
    )
}