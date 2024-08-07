package websocket;

import com.google.gson.Gson;
import dataaccess.DBAuthDAO;
import dataaccess.DBGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.ConnectUGC;
import websocket.commands.UserGameCommand;
import websocket.messages.*;


import java.io.IOException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {

    DBAuthDAO authDAO = new DBAuthDAO();
    DBGameDAO gameDAO = new DBGameDAO();
    WebSocketSessions sessions = new WebSocketSessions();

    @OnWebSocketConnect
    public void onConnect(Session session) {

    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {

    }

    @OnWebSocketError
    public void onError(Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand.CommandType commandType = new Gson().fromJson(message, UserGameCommand.class).getCommandType();
        switch (commandType) {
            case CONNECT -> connectReceiver(session, new Gson().fromJson(message, ConnectUGC.class));
            case MAKE_MOVE -> makeMoveReceiver(message);
            case LEAVE -> leaveGameReceiver(message);
            case RESIGN -> resignGameReceiver(message);
        }
    }


    private void connectReceiver(Session session, ConnectUGC command) throws IOException {
        String authToken = command.getAuthToken();
        String rootUsername = authDAO.getAuth(authToken).username();

        int gameID = command.getGameID();
        GameData game = gameDAO.getGame(gameID);

        String color = "BLACK";
        if (Objects.equals(game.getWhiteUsername(), rootUsername)) {
            color = "WHITE";
        }

        String gameName = gameDAO.getGame(gameID).getGameName();
        sessions.addSessionToGame(gameID, session);
        
        String message = String.format("%s joined game < %s > as the %s player", rootUsername, gameName, color);
        NotificationSM notification = new NotificationSM(
                ServerMessage.ServerMessageType.NOTIFICATION,
                message
        );
        sessions.sendGameMessage(new Gson().toJson(notification), gameID, session);
    }

    private void makeMoveReceiver(String message) {

    }

    private void leaveGameReceiver(String message) {

    }

    private void resignGameReceiver(String message) {

    }



}
