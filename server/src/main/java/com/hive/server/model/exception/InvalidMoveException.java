package com.hive.server.model.exception;

import com.hive.server.model.enums.Colour;

public class InvalidMoveException extends RuntimeException {

    public InvalidMoveException(Colour mover) {
        super("Invalid move by: " + mover.toString());
    }
}
