package chess;

import java.io.Serializable;

/**
 * Indicates an invalid move was made in a game
 */
public class InvalidMoveException extends Exception {

    public InvalidMoveException() {}

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String s,
                                ChessGame.TeamColor teamColor,
                                ChessPiece.PieceType promo,
                                Serializable serializable,
                                Serializable serializable1) {
    }
}
