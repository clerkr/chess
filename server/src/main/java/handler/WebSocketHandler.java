package handler;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;


@WebSocket
public class WebSocketHandler {

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
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(message);
            case MAKE_MOVE -> makeMove(message);
            case LEAVE -> leaveGame(message);
            case RESIGN -> resignGame(message);
        }
    }

    // DETERMINE THE RETURN TYPES OF THESE METHODS !!!!!!!!

    private void connect(String message) {

    }

    private void makeMove(String message) {

    }

    private void leaveGame(String message) {

    }

    private void resignGame(String message) {

    }



}
