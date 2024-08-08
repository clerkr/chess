package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DBAuthDAO;
import dataaccess.DBGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.ConnectUGC;
import websocket.commands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    DBAuthDAO authDAO = new DBAuthDAO();
    DBGameDAO gameDAO = new DBGameDAO();
    WebSocketSessions sessions = new WebSocketSessions();

    @OnWebSocketConnect
    public void onConnect(Session session) {

    }

    @OnWebSocketClose
    public void onClose(Session session) {

    }

    @OnWebSocketError
    public void onError(Throwable throwable) {

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand.CommandType commandType = new Gson().fromJson(message, UserGameCommand.class).getCommandType();
        switch (commandType) {
            case CONNECT -> connect(session, new Gson().fromJson(message, ConnectUGC.class));
            case MAKE_MOVE -> makeMove(message);
            case LEAVE -> leaveGame(message);
            case RESIGN -> resignGame(message);
        }
    }


    private void connect(Session session, ConnectUGC command) throws IOException {
        String authToken = command.getAuthToken();
        String rootUsername = authDAO.getAuth(authToken).username();
        int gameID = command.getGameID();
        ChessGame.TeamColor color = command.getTeamColor();
        String gameName = gameDAO.getGame(gameID).getGameName();

        sessions.addSessionToGame(gameID, session);
        String message = String.format("%s joined game < %s > as the %s player", rootUsername, gameName, color);
        sessions.sendGameMessage(message, gameID);
    }

    private void makeMove(String message) {

    }

    private void leaveGame(String message) {

    }

    private void resignGame(String message) {

    }



}
