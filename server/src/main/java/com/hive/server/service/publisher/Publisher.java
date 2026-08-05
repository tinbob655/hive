package com.hive.server.service.publisher;

import com.hive.server.model.dto.FrontendBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class Publisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publish(FrontendBoard frontendBoard) {
        this.messagingTemplate.convertAndSend("/topic/board", frontendBoard);
    }
}
