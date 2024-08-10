package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.ResponseException;
import model.GameData;
import ui.GameHandler;
import websocket.commands.*;
import websocket.messages.ErrorSM;
import websocket.messages.LoadGameSM;
import websocket.messages.NotificationSM;
import websocket.messages.ServerMessage;

//import org.eclipse.jetty.websocket.api.Session;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    GameHandler gameHandler;
    int port;

    public WebSocketFacade(GameHandler gameHandler, int port) {
        try {
            this.port = port;
            String url = String.format("ws://localhost:%d/ws", port);
            URI socketURI = new URI(url);

            this.gameHandler = gameHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler( new MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String message) {
                    ServerMessage.ServerMessageType serverMessageType =
                            new Gson().fromJson(message, ServerMessage.class).getServerMessageType();

                    switch (serverMessageType) {
                        case LOAD_GAME -> loadGameReceiver(session, new Gson().fromJson(message, LoadGameSM.class));
                        case NOTIFICATION -> notificationReceiver(session, new Gson().fromJson(message, NotificationSM.class));
                        case ERROR -> errorReceiver(session, new Gson().fromJson(message, ErrorSM.class));
                    }

                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {

             System.out.println(new ResponseException(500, ex.getMessage()));

        }
    }


    public void loadGameReceiver(Session session, LoadGameSM loadGameSM) {
        GameData game = loadGameSM.getGame();
        gameHandler.updateGame(game);
    }

    public void notificationReceiver(Session session, NotificationSM notification) {
        gameHandler.printMessage(notification.getMessage());
    }

    public void errorReceiver(Session session, ErrorSM errorSM) {
        System.out.println(errorSM.getErrorMessage());
    }

    // ---Outbound messages for the server--
    public void connectSender(String authToken, int gameID) throws ResponseException {
        try {
            ConnectUGC ugc = new ConnectUGC(
                    UserGameCommand.CommandType.CONNECT,
                    authToken,
                    gameID
            );
            this.session.getBasicRemote().sendText(new Gson().toJson(ugc));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void makeMoveSender(String authToken, int gameID, ChessMove move) {
        try {
            MakeMoveUGC ugc = new MakeMoveUGC(
                    UserGameCommand.CommandType.MAKE_MOVE,
                    authToken,
                    gameID,
                    move
            );
            this.session.getBasicRemote().sendText(new Gson().toJson(ugc));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void leaveGameSender(String authToken, int gameID) {
        try {
            LeaveGameUGC ugc = new LeaveGameUGC(
                    UserGameCommand.CommandType.LEAVE,
                    authToken,
                    gameID
            );
            this.session.getBasicRemote().sendText(new Gson().toJson(ugc));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resignGameSender(String authToken, int gameID) {
        try {
            ResignGameUGC ugc = new ResignGameUGC(
                    UserGameCommand.CommandType.RESIGN,
                    authToken,
                    gameID
            );
            this.session.getBasicRemote().sendText(new Gson().toJson(ugc));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // -------------------------------------

    public void drawGameUIBoardHandler() {
        gameHandler.drawBoard();
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }




}
