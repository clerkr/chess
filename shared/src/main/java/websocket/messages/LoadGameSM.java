package websocket.messages;

import chess.ChessGame;

public class LoadGameSM extends ServerMessage{

    ChessGame game;

    public LoadGameSM(ServerMessageType type, String message, ChessGame game) {
        super(type, message);
        this.game = game;
    }
}
