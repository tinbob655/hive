package com.hive.server.service.engine;

import com.hive.server.model.exception.InvalidMoveException;
import com.hive.server.model.move.Move;
import com.hive.server.model.state.GameState;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public  interface Engine {

    GameState handleHumanMove(Move move) throws InvalidMoveException;

    @Async
    void playBotTurn();

    GameState getState();
}
