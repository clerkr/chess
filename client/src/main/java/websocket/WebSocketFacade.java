package websocket;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {

    Session session;
    GameHandler gameHandler;

    public WebSocketFacade(String url, GameHandler gameHandler) {

    }





    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    @Override
    public void onMessage(String s) {

    }
}
