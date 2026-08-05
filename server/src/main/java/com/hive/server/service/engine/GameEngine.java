package com.hive.server.service.engine;

import com.hive.server.model.board.Board;
import com.hive.server.model.enums.Colour;
import com.hive.server.model.move.Move;
import com.hive.server.model.state.GameState;
import com.hive.server.service.bot.Bot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class GameEngine implements Engine {

    private final Board board = new Board();
    private Colour turn = Colour.WHITE;
    private GameState state = this.computeState();

    private final Bot bot;

    @Override
    public void handleHumanMove(Move move) {

    }

    private void advance(Move move) {

    }

    private GameState computeState() {
        return new GameState(
                this.board,
                this.turn
        );
    }
}
