import {useEffect, useState, useRef} from 'react';
import useAxios from "./useAxios.ts";
import {Client, type IFrame, type IMessage} from "@stomp/stompjs";
import SockJS from 'sockjs-client';
import type {Board} from "../types/board";
import type {Move} from "../types/move";

interface Exports {
    connected: boolean;
    board: Board;

    sendMove(move: Move): void;
}

const blankBoard: Board = {
    cells: [],
}

export default function useBoard(): Exports {

    const {axiosClient} = useAxios();

    const [board, setBoard] = useState<Board>(blankBoard);
    const [connected, setConnected] = useState<boolean>(false);

    const clientRef = useRef<Client|null>(null);

    //set up our websocket
    useEffect(() => {
        let isMounted: boolean = true;

        const client = new Client({
            webSocketFactory: () => new SockJS(`${import.meta.env.VITE_API_BASE_URL}/ws`),
            reconnectDelay: 5_000,

            onConnect: () => {
                if (!isMounted) return;

                setConnected(true);
                client.subscribe("/topic/board", (message: IMessage): void => {
                    const board: Board = JSON.parse(message.body) as Board;
                    setBoard(board);
                })
            },

            onDisconnect: () => {
                if (isMounted) {
                    setConnected(false);
                }
            },

            onStompError: (frame: IFrame) => console.error('STOMP error: ', frame),
        });

        clientRef.current = client;
        client.activate();

        //cleanup
        return () => {
            isMounted = false;
            void client.deactivate();
            setBoard(blankBoard);
            clientRef.current = null;
        }
    }, [axiosClient]);

    function sendMove(move: Move): void {
        clientRef.current?.publish({
            destination: "/app/move",
            body: JSON.stringify(move),
        });
    }

    return {connected, board, sendMove};
}