package com.hive.server.controller.socket;

import com.hive.server.model.move.Move;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;

public interface WebSocketControllerTemplate {

    @MessageMapping("/move")
    ResponseEntity<String> handleMove(Move move);
}
