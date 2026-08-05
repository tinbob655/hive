import {useEffect, useState} from 'react';
import useAxios from "./useAxios.ts";
import {Client, type IFrame, type IMessage} from "@stomp/stompjs";
import SockJS from 'sockjs-client';

interface Exports {
    connected: boolean;
    gameState: unknown;
}

export default function useGameState(): Exports {

    const {axiosClient} = useAxios();

    const [gameState, setGameState] = useState<unknown>(null);
    const [connected, setConnected] = useState<boolean>(false);

    function handleIncoming(gameState: unknown[]): void {}

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
                    handleIncoming(JSON.parse(message.body) as unknown[]);
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
            setGameState(null);
        }
    }, [axiosClient]);

    return {connected, gameState};
}