import React from 'react';
import PageHeader from "../../components/pageHeader/PageHeader.tsx";

export default function Home(): React.ReactElement {

    return (
        <React.Fragment>
            <PageHeader title={"Hive"} subtitle={"The classic hive game"} />
        </React.Fragment>
    )
}