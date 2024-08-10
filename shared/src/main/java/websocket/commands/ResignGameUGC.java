package websocket.commands;

public class ResignGameUGC extends UserGameCommand {

    public ResignGameUGC(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }
}
