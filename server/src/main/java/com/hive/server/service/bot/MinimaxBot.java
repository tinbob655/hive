package com.hive.server.service.bot;

import com.hive.server.model.move.Move;
import com.hive.server.model.state.GameState;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public  class MinimaxBot implements Bot {

    @Override
    public Move decideMove(GameState state) {
        return null;
    }
}
