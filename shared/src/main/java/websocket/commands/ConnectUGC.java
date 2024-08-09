package websocket.commands;

import chess.ChessGame;

public class ConnectUGC extends UserGameCommand {


    public ConnectUGC(
            CommandType commandType,
            String authToken,
            Integer gameID) {

        super(commandType, authToken, gameID);
    }


}
