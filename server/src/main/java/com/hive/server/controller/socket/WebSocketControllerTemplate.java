package com.hive.server.controller.socket;

import com.hive.server.model.board.Board;
import com.hive.server.model.move.Move;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

public interface WebSocketControllerTemplate {

    @MessageMapping("/move")
    @SendTo("/topic/orders")
    Board handleMove(Move move);
}
