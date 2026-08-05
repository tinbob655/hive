package com.hive.server.service.validator;

import com.hive.server.model.move.Move;
import com.hive.server.model.state.GameState;

public interface MoveValidator {

    boolean validate(Move move, GameState state);
}
