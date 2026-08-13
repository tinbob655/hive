package com.hive.server.service.publisher;

import com.hive.server.model.dto.FrontendBoard;
import com.hive.server.model.dto.GameOverInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class Publisher {

    private final SimpMessagingTemplate messagingTemplate;

    //overloaded so routing seems automatic
    public void publish(FrontendBoard frontendBoard) {
        this.messagingTemplate.convertAndSend("/topic/board", frontendBoard);
    }
    public void publish(GameOverInfo gameOver) {
        this.messagingTemplate.convertAndSend("/topic/gameOver", gameOver);
    }
}
