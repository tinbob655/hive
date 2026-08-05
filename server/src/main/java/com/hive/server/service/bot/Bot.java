package com.hive.server.service.bot;

import com.hive.server.model.move.Move;
import com.hive.server.model.state.GameState;
import org.springframework.stereotype.Service;

@Service
public interface Bot {

    Move decideMove(GameState state);
}
