package websocket.messages;

import chess.ChessGame;

public class LoadGameSM extends ServerMessage{


    public LoadGameSM(ServerMessageType type, String message) {
        super(type, message);
    }
}
