package websocket;

import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;



public class WebSocketSessions {
    public ConcurrentHashMap<Integer, HashSet<Session>> sessionMap = new ConcurrentHashMap<>();
//    public HashSet<Session> sessionSet = new HashSet<>();

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
        sessionMap.get(gameID).remove(session);
    }

    public void removeSession(Session session) {
        for (int gameID : sessionMap.keySet()) {
            HashSet<Session> sessions = sessionMap.get(gameID);
            sessions.remove(session);
        }
    }

    public HashSet<Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }

    public void sendGameMessage(String message, int gameID) throws IOException
    {
        HashSet<Session> gameSessions = sessionMap.get(gameID);
        for (Session session : gameSessions)
        {
            if (!session.isOpen()) { continue; }
            session.getRemote().sendString(message);
        }
    }

    // I do not believe that I need a broadcast method. If I do, make a set of all Sessions

}
