package com.hive.server.service.bot;

import com.hive.server.model.move.Move;
import com.hive.server.model.state.GameState;
import org.springframework.stereotype.Service;

@Service
public final class MinimaxBot implements Bot {

    @Override
    public Move decideMove(GameState state) {
        return null;
    }
}
