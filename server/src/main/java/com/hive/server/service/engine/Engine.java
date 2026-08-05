package com.hive.server.service.engine;

import com.hive.server.model.move.Move;
import org.springframework.stereotype.Service;

@Service
public sealed interface Engine permits GameEngine {

    void handleHumanMove(Move move);
}
