package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.ResponseException;
import ui.DrawPrompt;
import websocket.commands.ConnectUGC;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameSM;
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
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    System.out.print("\r\033[K");
                    System.out.println(serverMessage.getMessage());
                    DrawPrompt.drawLoggedInPrompt();
//                    switch (serverMessageType) {
//                    }

                }
            }); // Now finish the onMessage to make this work

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }



    public void loadGameReciever(LoadGameSM serverMessage) {

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

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
    // -------------------------------------




}
