package com.hive.server.controller.socket;

import com.hive.server.model.board.Board;
import com.hive.server.model.move.Move;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public final class WebSocketController implements WebSocketControllerTemplate {

    @MessageMapping("/move")
    public @NonNull Board handleMove(Move move) {
        return null;
    }
}
