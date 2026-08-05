package com.hive.server.controller.legalMoves;

import com.hive.server.model.board.HexCoordinate;
import com.hive.server.model.enums.Bug;
import com.hive.server.model.enums.Colour;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

public interface LegalMovesControllerTemplate {

    @GetMapping("/board")
    Set<HexCoordinate> getBoardMoves(@RequestParam HexCoordinate coord);

    @GetMapping("/bank")
    Set<HexCoordinate> getBankMoves(@RequestParam Bug bug, @RequestParam Colour colour);
}
