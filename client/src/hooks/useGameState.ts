import {useEffect, useState} from 'react';
import useAxios from "./useAxios.ts";
import {Client, type IFrame, type IMessage} from "@stomp/stompjs";
import SockJS from 'sockjs-client';
import type {Board} from "../types/board";

interface Exports {
    connected: boolean;
    board: Board;
}

const blankBoard: Board = {
    cells: [],
}

export default function useGameState(): Exports {

    const {axiosClient} = useAxios();

    const [board, setBoard] = useState<Board>(blankBoard);
    const [connected, setConnected] = useState<boolean>(false);

    //set up our websocket
    useEffect(() => {
        let isMounted: boolean = true;

        const client = new Client({
            webSocketFactory: () => new SockJS(`${import.meta.env.VITE_API_BASE_URL}/ws`),
            reconnectDelay: 5_000,

            onConnect: () => {
                if (!isMounted) return;

                setConnected(true);
                client.subscribe("/topic/orders", (message: IMessage): void => {
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

        client.activate();

        //cleanup
        return () => {
            isMounted = false;
            void client.deactivate();
            setBoard(blankBoard);
        }
    }, [axiosClient]);

    return {connected, board};
}