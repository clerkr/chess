package websocket.messages;

import chess.ChessGame;

public class LoadGameSM extends ServerMessage{

    private ChessGame game;

    public LoadGameSM(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
