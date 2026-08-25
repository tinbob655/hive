import {useEffect, useState} from 'react';
import useAxios from "./useAxios.ts";
import type {Bug, HexCoordinate} from "../types/board";
import type {Move} from "../types/move";

export type Selection =
    | { type: 'board'; coordinate: HexCoordinate }
    | { type: 'bank'; bug: Bug };

interface Exports {
    legalMoves: Move[];
    loading: boolean;
    noMovesAvailable: boolean;
}

export default function useLegalMoves(selection: Selection | null): Exports {

    const {axiosClient} = useAxios();
    const [serverLegalMoves, setServerLegalMoves] = useState<Move[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [noMovesAvailable, setNoMovesAvailable] = useState<boolean>(false);

    const legalMoves: Move[] = selection ? serverLegalMoves : [];

    //gets the legal moves for a selection
    useEffect(() => {
        if (!selection) {
            return;
        }

        let cancelled: boolean = false;

        const getLegalMoves = () => {
            setLoading(true);

            const request = selection.type === 'board'
                ? axiosClient.get<Move[]>('/api/moves/board', {
                    params: {q: selection.coordinate.q, r: selection.coordinate.r},
                })
                : axiosClient.get<Move[]>('/api/moves/bank', {
                    params: {bug: selection.bug, colour: 'WHITE'},
                });

            request
                .then((response) => {
                    if (!cancelled) {
                        setServerLegalMoves(response.data);
                        setNoMovesAvailable(response.data.length === 0);
                    }
                })
                .catch((error) => {
                    console.error('Failed to fetch legal moves', error);
                    if (!cancelled) setServerLegalMoves([]);
                })
                .finally(() => {
                    if (!cancelled) setLoading(false);
                });
        }

        getLegalMoves();

        return () => {
            cancelled = true;
        };
    }, [selection, axiosClient]);

    return {legalMoves, loading, noMovesAvailable};
}