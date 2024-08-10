package websocket.messages;

import model.GameData;

public class LoadGameSM extends ServerMessage{

    private GameData game;

    public LoadGameSM(ServerMessageType type, GameData game) {
        super(type);
        this.game = game;
    }

    public GameData getGame() {
        return game;
    }
}
