package com.hive.server.config;

import com.hive.server.model.exception.InvalidMoveException;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @MessageExceptionHandler(InvalidMoveException.class)
    @SendToUser("/topic/moveError")
    public String handleInvalidMove(InvalidMoveException e) {
        System.err.println(e.getMessage());
        return e.getMessage();
    }
}
