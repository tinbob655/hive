package com.hive.server.controller.legalMoves;

import com.hive.server.model.enums.Bug;
import com.hive.server.model.enums.Colour;
import com.hive.server.model.move.Move;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

public interface LegalMovesControllerTemplate {

    @GetMapping("/board")
    Set<Move> getBoardMoves(@RequestParam int q, @RequestParam int r);

    @GetMapping("/bank")
    Set<Move> getBankMoves(@RequestParam Bug bug, @RequestParam Colour colour);
}
