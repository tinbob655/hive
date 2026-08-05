import React from 'react';
import type {Bug} from "../../types/board";
import type {BankEntry} from "../../utils/moves.ts";

const BUG_LABELS: Record<Bug, string> = {
    BEE: 'Queen Bee',
    BEETLE: 'Beetle',
    GRASSHOPPER: 'Grasshopper',
    SPIDER: 'Spider',
    ANT: 'Soldier Ant',
    LADYBUG: 'Ladybug',
    MOSQUITO: 'Mosquito',
    WOODLOUSE: 'Woodlouse',
};

interface Props {
    entries: BankEntry[];
    selectedBug: Bug | null;
    onSelect: (bug: Bug) => void;
}

export default function PieceBank({entries, selectedBug, onSelect}: Props): React.ReactElement {

    return (
        <div className={"pieceBank"}>
            {entries.map(({bug, count}) => (
                <button
                    key={bug}
                    type={"button"}
                    className={`pieceBank__piece${bug === selectedBug ? ' pieceBank__piece--selected' : ''}`}
                    onClick={() => onSelect(bug)}
                >
                    <span className={"pieceBank__label"}>{BUG_LABELS[bug]}</span>
                    <span className={"pieceBank__count"}>x{count}</span>
                </button>
            ))}
        </div>
    );
}