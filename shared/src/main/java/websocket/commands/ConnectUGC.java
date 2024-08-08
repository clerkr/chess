package websocket.commands;

import chess.ChessGame;

public class ConnectUGC extends UserGameCommand {

    private final ChessGame.TeamColor teamColor;

    public ConnectUGC(
            CommandType commandType,
            String authToken,
            Integer gameID,
            ChessGame.TeamColor teamColor) {

        super(commandType, authToken, gameID);
        this.teamColor = teamColor;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

}
