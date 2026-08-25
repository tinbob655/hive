package com.hive.server.service.bot;

import com.hive.server.model.enums.Colour;
import com.hive.server.model.move.Move;
import com.hive.server.model.move.SkipMove;
import com.hive.server.model.state.GameState;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Fallback
@Qualifier("random")
public final class RandomBot implements Bot {

    @Override
    public Move decideMove(GameState state) {
        Set<Move> legalMoves = state.legalMoves();
        return legalMoves.stream().findAny().orElse(new SkipMove(Colour.BLACK));
    }
}
