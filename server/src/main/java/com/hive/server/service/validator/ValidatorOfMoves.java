package com.hive.server.service.validator;

import com.hive.server.model.move.Move;
import com.hive.server.model.state.GameState;
import org.springframework.stereotype.Service;

@Service
public final class ValidatorOfMoves implements MoveValidator {


    @Override
    public boolean validate(Move move, GameState state) {
        return state.legalMoves().contains(move);
    }
}
