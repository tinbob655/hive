package com.hive.server.service.engine;

import com.hive.server.model.board.Board;
import com.hive.server.model.board.Piece;
import com.hive.server.model.enums.Colour;
import com.hive.server.model.exception.InvalidMoveException;
import com.hive.server.model.move.Move;
import com.hive.server.model.move.PlaceMove;
import com.hive.server.model.move.RelocateMove;
import com.hive.server.model.move.SkipMove;
import com.hive.server.model.state.GameState;
import com.hive.server.service.bot.Bot;
import com.hive.server.service.validator.MoveValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class GameEngine implements Engine {

    private final Board board = new Board();
    private Colour turn = Colour.WHITE;

    @Getter
    private volatile GameState state = this.computeState();

    private final Bot bot;
    private final MoveValidator moveValidator;

    @Override
    public GameState handleHumanMove(Move move) throws InvalidMoveException {

        //only do a move if it is valid
        boolean isValidMove = this.moveValidator.validate(move, this.state);
        if (!isValidMove) throw new InvalidMoveException(Colour.WHITE);
        this.advance(move);

        return this.state;
    }

    @Override
    @Async
    public CompletableFuture<GameState> playBotTurn() {
        Move botMove = this.bot.decideMove(this.state);

        boolean isValid = this.moveValidator.validate(botMove, this.state);
        if (!isValid) throw new InvalidMoveException(Colour.BLACK);

        this.advance(botMove);
        return CompletableFuture.completedFuture(this.state);
    }


    private synchronized void advance(@NonNull Move move) {

        //we know that the move is valid already
        switch (move) {
            case PlaceMove mv -> this.board.addPiece(new Piece(mv.bug(), mv.colour()), mv.destination());
            case RelocateMove mv -> this.board.movePiece(mv.from(), mv.to());
            case SkipMove ignored -> {}
        }

        this.turn = this.turn == Colour.WHITE ? Colour.BLACK : Colour.WHITE;
        this.state = this.computeState();
    }

    private @NonNull GameState computeState() {
        return new GameState(
                this.board.copy(),
                this.turn
        );
    }
}
