package com.hive.server.service.bot;

import com.hive.server.model.move.Move;
import com.hive.server.model.state.GameState;
import org.springframework.stereotype.Service;

@Service
public sealed interface Bot permits MinimaxBot {

    Move decideMove(GameState state);
}
