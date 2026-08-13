import React, {useMemo, useState} from 'react';
import useBoard from "../../hooks/useBoard.ts";
import useLegalMoves, {type Selection} from "../../hooks/useLegalMoves.ts";
import {axialToPixel, coordKey, HEX_SIZE} from "../../utils/hex.ts";
import {type BankEntry, getBank, moveDestination} from "../../utils/moves.ts";
import HexTile from "./HexTile.tsx";
import PieceBank from "./PieceBank.tsx";
import type {Bug, Cell, HexCoordinate} from "../../types/board";
import type {Move} from "../../types/move";
import './board.scss';
import {Bouncy} from "ldrs/react";
import 'ldrs/react/Bouncy.css';

const PADDING: number = HEX_SIZE * 2;

export default function Board(): React.ReactElement {

    const {board, connected, error, botThinking, isHumanTurn, winner, sendMove} = useBoard();
    const [selection, setSelection] = useState<Selection | null>(null);
    const [gameStarted, setGameStarted] = useState<boolean>(false);
    const {legalMoves} = useLegalMoves(selection);

    const bank: BankEntry[] = useMemo(() => getBank(board, 'WHITE'), [board]);

    //index legal moves by destination so lookups while rendering are O(1)
    const movesByDestination: Map<string, Move> = useMemo(() => {
        const map = new Map<string, Move>();
        legalMoves.forEach((move) => map.set(coordKey(moveDestination(move)), move));
        return map;
    }, [legalMoves]);

    const occupiedKeys: Set<string> = useMemo(
        () => new Set(board.cells.map((cell) => coordKey(cell.coordinate))),
        [board]
    );

    //legal destinations that aren't an existing cell (i.e. empty spaces around the hive)
    const ghostCells: HexCoordinate[] = useMemo(
        (): HexCoordinate[] => legalMoves
            .map(moveDestination)
            .filter((coordinate) => !occupiedKeys.has(coordKey(coordinate))),
        [legalMoves, occupiedKeys]
    );

    //work out the SVG viewBox from whatever's actually on screen
    const bounds = useMemo(() => {
        const points = [
            ...board.cells.map((cell) => axialToPixel(cell.coordinate)),
            ...ghostCells.map((coordinate) => axialToPixel(coordinate)),
        ];

        if (points.length === 0) {
            return {minX: -HEX_SIZE, minY: -HEX_SIZE, maxX: HEX_SIZE, maxY: HEX_SIZE};
        }

        return {
            minX: Math.min(...points.map((p) => p.x)),
            minY: Math.min(...points.map((p) => p.y)),
            maxX: Math.max(...points.map((p) => p.x)),
            maxY: Math.max(...points.map((p) => p.y)),
        };
    }, [board, ghostCells]);

    const boardWidth: number = bounds.maxX - bounds.minX + PADDING * 2;
    const boardHeight: number = bounds.maxY - bounds.minY + PADDING * 2;

    function handleCellClick(cell: Cell): void {

        //don't let the human click cells if it's not their turn
        if (!isHumanTurn) return;

        //clicking a highlighted destination that already holds a piece (a beetle climb) plays that move
        const move = movesByDestination.get(coordKey(cell.coordinate));
        if (move) {
            sendMove(move);
            setSelection(null);
            return;
        }

        //otherwise, only white's own pieces can be picked up
        const topPiece = cell.pieces[cell.pieces.length - 1];
        if (!topPiece || topPiece.colour !== 'WHITE') return;

        setSelection((current) =>
            current?.type === 'board' && coordKey(current.coordinate) === coordKey(cell.coordinate)
                ? null
                : {type: 'board', coordinate: cell.coordinate}
        );
    }

    function handleGhostClick(coordinate: HexCoordinate): void {

        //don't let the human do any moves if it's not their turn
        if (!isHumanTurn) return;

        const move = movesByDestination.get(coordKey(coordinate));
        if (!move) return;
        sendMove(move);
        setSelection(null);
    }

    function handleBankSelect(bug: Bug): void {

        //don't let the human do any moves if it's not their turn
        if (!isHumanTurn) return;

        setSelection((current) =>
            current?.type === 'bank' && current.bug === bug ? null : {type: 'bank', bug}
        );
    }

    //hide the game until the user chooses to start the game. Note that the backend game starts on boot regardless
    if (!gameStarted) {
        return (
            <div className={"gameBoard gameBoard--start"}>
                <p className={"gameBoard__status"}>
                    {error ? `Connection failed: ${error}` : connected ? 'Ready to play' : 'Connecting...'}
                </p>
                <button
                    type={"button"}
                    className={"gameBoard__startButton"}
                    disabled={!connected}
                    onClick={() => setGameStarted(true)}
                >
                    Start Game
                </button>
            </div>
        );
    }

    return (
        <div className={"gameBoard"}>
            {winner ? (
                <h2>
                    Game over! {winner} won!
                </h2>
            ) : botThinking ? (
                <div style={{display: 'flex', gap: '1rem'}}>
                    <p className={"gameBoard__status--botTurn"}>
                        Thinking...
                    </p>
                    <Bouncy color={"#d89d24"}/>
                </div>
            ) : (
                <p className={`gameBoard__status${isHumanTurn ? ' gameBoard__status--yourTurn' : ' gameBoard__status--botTurn'}`}>
                    {!connected
                        ? (error ? `Connection lost: ${error}` : 'Reconnecting...')
                        : isHumanTurn
                            ? 'Your turn'
                            : "Waiting on the bot's turn..."}
                </p>
            )}

            <svg
                width={boardWidth}
                height={boardHeight}
                className={"gameBoard__svg"}
                viewBox={`${bounds.minX - PADDING} ${bounds.minY - PADDING} ${bounds.maxX - bounds.minX + PADDING * 2} ${bounds.maxY - bounds.minY + PADDING * 2}`}
            >
                {board.cells.map((cell) => {
                    const {x, y} = axialToPixel(cell.coordinate);
                    const topPiece = cell.pieces[cell.pieces.length - 1];
                    const key = coordKey(cell.coordinate);
                    const isSelected = selection?.type === 'board' && coordKey(selection.coordinate) === key;
                    const isMoveTarget = movesByDestination.has(key);

                    return (
                        <HexTile
                            key={key}
                            x={x}
                            y={y}
                            size={HEX_SIZE}
                            bug={topPiece?.bug}
                            colour={topPiece?.colour}
                            stackCount={cell.pieces.length}
                            isSelected={isSelected}
                            isMoveTarget={isMoveTarget}
                            isClickable={isHumanTurn && (topPiece?.colour === 'WHITE' || isMoveTarget)}
                            onClick={() => handleCellClick(cell)}
                        />
                    );
                })}

                {ghostCells.map((coordinate) => {
                    const {x, y} = axialToPixel(coordinate);
                    return (
                        <HexTile
                            key={coordKey(coordinate)}
                            x={x}
                            y={y}
                            size={HEX_SIZE}
                            isMoveTarget
                            isClickable={isHumanTurn}
                            onClick={() => handleGhostClick(coordinate)}
                        />
                    );
                })}
            </svg>

            <PieceBank
                entries={bank}
                selectedBug={selection?.type === 'bank' ? selection.bug : null}
                disabled={!isHumanTurn}
                onSelect={handleBankSelect}
            />
        </div>
    );
}