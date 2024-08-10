package websocket.commands;

public class LeaveGameUGC extends UserGameCommand {

    public LeaveGameUGC(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }
}
