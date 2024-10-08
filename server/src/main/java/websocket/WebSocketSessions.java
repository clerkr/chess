package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;



public class WebSocketSessions {
    private ConcurrentHashMap<Integer, HashSet<Session>> sessionMap = new ConcurrentHashMap<>();

    public void addSessionToGame(int gameID, Session session) {
        if (sessionMap.containsKey(gameID)) {
            sessionMap.get(gameID).add(session);
        } else {
            HashSet<Session> sessions = new HashSet<>();
            sessions.add(session);
            sessionMap.put(gameID, sessions);
        }
    }

    public void removeSessionFromGame(int gameID, Session session) {
        if (sessionMap.containsKey(gameID)) {
            sessionMap.get(gameID).remove(session);
        }
    }


    public HashSet<Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }

    public void sendGameMessageExclusive(ServerMessage message, int gameID, Session rootSession) {

        HashSet<Session> gameSessions = getSessionsForGame(gameID);
        for (Session session : gameSessions) {
            if (!session.isOpen() || session == rootSession) { continue; }
            sendSessionMessage(message, session);
        }

    }

    public void sendGameMessageInclusive(ServerMessage message, int gameID) {

        HashSet<Session> gameSessions = getSessionsForGame(gameID);
        for (Session session : gameSessions) {
            sendSessionMessage(message, session);
        }

    }


    public void sendSessionMessage(ServerMessage message, Session session) {
        try {
            String messageJson = new Gson().toJson(message);
            session.getRemote().sendString(messageJson);
        } catch (Exception e) {
            System.out.println("Failed to send client a message: " + e.getMessage());
        }
    }


}
