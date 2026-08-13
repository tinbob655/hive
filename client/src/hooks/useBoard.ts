import {useEffect, useState, useRef} from 'react';
import useAxios from "./useAxios.ts";
import {Client, type IFrame, type IMessage} from "@stomp/stompjs";
import SockJS from 'sockjs-client';
import type {Board, Colour} from "../types/board";
import type {Move} from "../types/move";
import type {GameOverInfo} from "../types/gameOver";

interface Exports {
    connected: boolean;
    board: Board;
    error: string | null;
    isHumanTurn: boolean;
    botThinking: boolean;
    winner: Colour | null;

    sendMove(move: Move): void;
}

const blankBoard: Board = {
    cells: [],
}

export default function useBoard(): Exports {

    const {axiosClient} = useAxios();

    const [board, setBoard] = useState<Board>(blankBoard);
    const [connected, setConnected] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [winner, setWinner] = useState<Colour | null>(null);

    //the human player is always white, and the backend's game starts with white to move
    const [isYourTurn, setIsYourTurn] = useState<boolean>(true);
    const [botThinking, setBotThinking] = useState<boolean>(false);

    const clientRef = useRef<Client|null>(null);

    //tracks whether the next /topic/board message is the echo of our own move, or the bot's reply
    const awaitingOwnMoveEcho = useRef<boolean>(false);

    useEffect(() => {
        let isMounted: boolean = true;

        const client = new Client({
            webSocketFactory: () => new SockJS(`${import.meta.env.VITE_API_BASE_URL}/ws`),
            reconnectDelay: 5_000,

            onConnect: () => {
                if (!isMounted) return;

                setConnected(true);
                setError(null);

                //listens for server moves
                client.subscribe("/topic/board", (message: IMessage): void => {
                    const board: Board = JSON.parse(message.body) as Board;
                    setBoard(board);

                    if (awaitingOwnMoveEcho.current) {

                        //this is our own move being accepted
                        awaitingOwnMoveEcho.current = false;
                        setBotThinking(true);
                    }
                    else {

                        //this is the server responding with the bot's move
                        setBotThinking(false);
                        setIsYourTurn(true);
                    }
                });

                //listens for game over
                client.subscribe("/topic/gameOver", (message: IMessage): void => {
                    const gameOverInfo: GameOverInfo = JSON.parse(message.body) as GameOverInfo;
                    setWinner(gameOverInfo.winner);
                    setBotThinking(false);
                    setIsYourTurn(false);
                });
            },

            onDisconnect: () => {
                if (isMounted) {
                    setConnected(false);
                }
            },

            onStompError: (frame: IFrame) => {
                console.error('STOMP error: ', frame);
                if (isMounted) setError(frame.headers['message'] ?? 'Connection error');
            },

            onWebSocketError: (event: Event) => {
                console.error('WebSocket error: ', event);
                if (isMounted) setError('Could not reach the game server');
            },
        });

        clientRef.current = client;
        client.activate();

        //cleanup
        return () => {
            isMounted = false;
            void client.deactivate();
            setBoard(blankBoard);
            setIsYourTurn(true);
            setBotThinking(false);
            awaitingOwnMoveEcho.current = false;
            clientRef.current = null;
        }
    }, [axiosClient]);

    function sendMove(move: Move): void {
        if (!clientRef.current) return;

        //optimistically flip turn state the moment we send, the server should confirm
        awaitingOwnMoveEcho.current = true;
        setIsYourTurn(false);
        setBotThinking(false);

        clientRef.current.publish({
            destination: "/app/move",
            body: JSON.stringify(move),
        });
    }

    return {connected, board, error, isHumanTurn: isYourTurn, botThinking, winner, sendMove};
}