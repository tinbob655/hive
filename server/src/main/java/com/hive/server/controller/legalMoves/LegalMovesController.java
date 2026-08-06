package com.hive.server.controller.legalMoves;

import com.hive.server.model.board.HexCoordinate;
import com.hive.server.model.enums.Bug;
import com.hive.server.model.enums.Colour;
import com.hive.server.model.move.Move;
import com.hive.server.model.move.PlaceMove;
import com.hive.server.model.move.RelocateMove;
import com.hive.server.model.state.GameState;
import com.hive.server.service.engine.Engine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/moves")
@RequiredArgsConstructor
public final class LegalMovesController implements LegalMovesControllerTemplate {

    private final Engine gameEngine;

    @Override
    public Set<Move> getBoardMoves(int q, int r) {
        HexCoordinate coord = new HexCoordinate(q, r);
        GameState state = this.gameEngine.getState();

        return state.legalMoves().stream()
                .filter(move -> move instanceof RelocateMove relocateMove && relocateMove.from().equals(coord))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Move> getBankMoves(Bug bug, Colour colour) {
        GameState state = this.gameEngine.getState();

        return state.legalMoves().stream()
                .filter(move -> move instanceof PlaceMove placeMove
                    && placeMove.bug() == bug
                    && placeMove.colour() == colour)
                .collect(Collectors.toSet());
    }
}
