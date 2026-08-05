package com.hive.server.controller.legalMoves;

import com.hive.server.model.board.HexCoordinate;
import com.hive.server.model.enums.Bug;
import com.hive.server.model.enums.Colour;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/api/moves")
public final class LegalMovesController implements LegalMovesControllerTemplate {

    @Override
    public Set<HexCoordinate> getBoardMoves(HexCoordinate coord) {
        return Set.of();
    }

    @Override
    public Set<HexCoordinate> getBankMoves(Bug bug, Colour colour) {
        return Set.of();
    }
}
