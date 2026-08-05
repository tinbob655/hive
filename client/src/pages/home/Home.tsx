import React from 'react';
import PageHeader from "../../components/pageHeader/PageHeader.tsx";
import Board from "../../components/board/Board.tsx";

export default function Home(): React.ReactElement {

    return (
        <React.Fragment>
            <PageHeader title={"Hive"} subtitle={"The classic hive game"} />

            <Board/>
        </React.Fragment>
    )
}