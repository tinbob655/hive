package com.hive.server.controller.socket;


import com.hive.server.model.dto.FrontendBoard;
import com.hive.server.model.dto.GameOverInfo;
import com.hive.server.model.enums.Colour;
import com.hive.server.model.move.Move;
import com.hive.server.model.state.GameState;
import com.hive.server.service.engine.Engine;
import com.hive.server.service.publisher.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public final class WebSocketController implements WebSocketControllerTemplate {

    private final Engine gameEngine;
    private final Publisher publisher;

    @MessageMapping("/move")
    public ResponseEntity<String> handleMove(Move move) {
        GameState state = this.gameEngine.getState();

        //the player can only go if it's their turn
        if (state.currentColour() != Colour.WHITE) return ResponseEntity.badRequest().body("Not your turn");

        GameState stateAfterHumanMove = this.gameEngine.
                handleHumanMove(move);

        //create the dto to send human move to the front
        FrontendBoard frontendBoard = stateAfterHumanMove.board().getFrontendBoard();
        this.publisher.publish(frontendBoard);

        //check for game over
        boolean isGameOver = stateAfterHumanMove.isGameOver();
        if (isGameOver) {
            this.publisher.publish(new GameOverInfo(Colour.WHITE));
            return ResponseEntity.ok().build();
        }

        //the bot's turn
        this.gameEngine.playBotTurn()
                .thenAccept(stateAfterBotMove -> {

                    //send bot's move to frontend
                    FrontendBoard botFrontendBoard = stateAfterBotMove.board().getFrontendBoard();
                    this.publisher.publish(botFrontendBoard);

                    //check for game over
                    boolean isBotGameOver = stateAfterBotMove.isGameOver();
                    if (isBotGameOver) {
                        this.publisher.publish(new GameOverInfo(Colour.BLACK));
                    }
                });

        return ResponseEntity.ok().build();
    }
}
