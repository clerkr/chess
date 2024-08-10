package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.ResponseException;
import websocket.commands.ConnectUGC;
import websocket.commands.MakeMoveUGC;
import websocket.commands.UserGameCommand;
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

//    public WebSocketFacade(GameHandler gameHandler, int port) throws ResponseException {
    public WebSocketFacade(int port) throws ResponseException {
        try {
            this.port = port;
            String url = String.format("ws://localhost:%d/ws", port);
            URI socketURI = new URI(url);

//            this.gameHandler = gameHandler;

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
                        case ERROR -> errorReceiver(session, new Gson().fromJson(message, ErrorSM.class);
                    }

                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }



    public void loadGameReceiver(Session session, LoadGameSM loadGameSM) {

    }

    public void notificationReceiver(Session session, NotificationSM notification) {

    }

    public void errorReceiver(Session session, ErrorSM errorSM) {

    }

    // Outbound messages for the server

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

    // -------------------------------------

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }




}