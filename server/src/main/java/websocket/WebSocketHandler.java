package websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DBAuthDAO;
import dataaccess.DBGameDAO;
import dataaccess.InvalidGameException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.*;


import java.util.HashMap;
import java.util.Map;
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
            case MAKE_MOVE -> makeMoveReceiver(session, new Gson().fromJson(message, MakeMoveUGC.class));
            case LEAVE -> leaveGameReceiver(session, new Gson().fromJson(message, LeaveGameUGC.class));
            case RESIGN -> resignGameReceiver(session, new Gson().fromJson(message, ResignGameUGC.class));
        }
    }

    private void connectReceiver(Session session, ConnectUGC command) {
        try {
            String authToken = command.getAuthToken();
            String rootUsername = authDAO.getAuth(authToken).username();

            int gameID = command.getGameID();
            GameData game = gameDAO.getGame(gameID);

            String color = "";
            if (Objects.equals(game.getBlackUsername(), rootUsername)) {
                color = "BLACK";
            } else if (Objects.equals(game.getWhiteUsername(), rootUsername)) {
                color = "WHITE";
            }

            String gameName = gameDAO.getGame(gameID).getGameName();
            sessions.addSessionToGame(gameID, session);

            String message;
            if (!color.isEmpty()) {
                message = String.format(
                        "%s joined the game (%s) as the %s player",
                        rootUsername, gameName, color
                );
            } else {
                message = String.format(
                        "%s joined the game (%s) as an observer",
                        rootUsername, gameName
                );
            }
            NotificationSM notification = new NotificationSM(
                    ServerMessage.ServerMessageType.NOTIFICATION,
                    message
            );
            sessions.sendGameMessageExclusive(notification, gameID, session);

            LoadGameSM loadGameSM = new LoadGameSM(ServerMessage.ServerMessageType.LOAD_GAME, game);
            sessions.sendSessionMessage(loadGameSM, session);
        } catch (Exception e) {
            sessions.sendSessionMessage(ErrorSM.prepareErrorSM(e), session);
        }
    }

    private void makeMoveReceiver(Session session, MakeMoveUGC command) {
        try {
            String authToken = command.getAuthToken();
            String rootUsername = authDAO.getAuth(authToken).username();

            int gameID = command.getGameID();
            GameData gameData = gameDAO.getGame(gameID);
            ChessGame game = gameData.getGame();

            if (game.getIsOver()) {
                throw new Exception("The game has already concluded");
            }

            ChessMove move = command.getMove();

            String message =
                    rootUsername + " move: " + game.getBoard().getPiece(move.getStartPosition()).getPieceType() + " " +
                    coordMaker(move.getStartPosition()) + " -> " + coordMaker(move.getEndPosition());

            game.makeMove(move);

            NotificationSM notification = new NotificationSM(
                    ServerMessage.ServerMessageType.NOTIFICATION,
                    message
            );
            sessions.sendGameMessageExclusive(notification, gameID, session);




            String statusMessage = "";
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                statusMessage = String.format("%s is in checkmate",
                        gameData.getWhiteUsername());
                game.setGameIsOver();
            } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                statusMessage = String.format("%s is in checkmate",
                        gameData.getBlackUsername());
                game.setGameIsOver();
            } else if (game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                statusMessage = "Stalemate reached";
                game.setGameIsOver();
            } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                statusMessage = String.format("%s is in check",
                        gameData.getWhiteUsername());
            } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                statusMessage = String.format("%s is in check",
                        gameData.getBlackUsername());
            }
            if (!statusMessage.isEmpty()) {
                NotificationSM statusNotification = new NotificationSM(
                        ServerMessage.ServerMessageType.NOTIFICATION,
                        statusMessage
                );
                sessions.sendGameMessageInclusive(statusNotification, gameID);
            }

            GameData updatedGame = new GameData(
                    gameID,
                    gameData.getWhiteUsername(),
                    gameData.getBlackUsername(),
                    gameData.getGameName(),
                    game
            );
            gameDAO.updateGame(updatedGame);

            LoadGameSM sm = new LoadGameSM(
                    ServerMessage.ServerMessageType.LOAD_GAME,
                    updatedGame
            );
            sessions.sendGameMessageInclusive(sm, gameID);



        } catch (Exception e) {
            sessions.sendSessionMessage(ErrorSM.prepareErrorSM(e), session);
        }
    }

    private void leaveGameReceiver(Session session, LeaveGameUGC command) {
        String authToken = command.getAuthToken();
        String rootUsername = authDAO.getAuth(authToken).username();
        int gameID = command.getGameID();
        String gameName = gameDAO.getGame(gameID).getGameName();

        sessions.removeSessionFromGame(gameID, session);

        String message = String.format(
                "%s left the game (%s)",
                rootUsername, gameName
        );
        NotificationSM notification = new NotificationSM(
                ServerMessage.ServerMessageType.NOTIFICATION,
                message
        );
        sessions.sendGameMessageExclusive(notification, gameID, session);
    }

    private void resignGameReceiver(Session session, ResignGameUGC command) {
        try {
            String authToken = command.getAuthToken();
            String rootUsername = authDAO.getAuth(authToken).username();
            int gameID = command.getGameID();
            GameData gameData = gameDAO.getGame(gameID);
            ChessGame game = gameData.getGame();
            String gameName = gameDAO.getGame(gameID).getGameName();

            game.setGameIsOver();

            GameData updatedGameData = new GameData(
                    gameID,
                    gameData.getWhiteUsername(),
                    gameData.getBlackUsername(),
                    gameData.getGameName(),
                    game);
            gameDAO.updateGame(updatedGameData);

            String message = String.format(
                    "%s resigned from the game (%s)",
                    rootUsername, gameName
            );
            NotificationSM notification = new NotificationSM(
                    ServerMessage.ServerMessageType.NOTIFICATION,
                    message
            );
            sessions.sendGameMessageInclusive(notification, gameID);

            LoadGameSM sm = new LoadGameSM(
                    ServerMessage.ServerMessageType.LOAD_GAME,
                    updatedGameData
            );
            sessions.sendGameMessageInclusive(sm, gameID);

        } catch (InvalidGameException e) {
            sessions.sendSessionMessage(ErrorSM.prepareErrorSM(e), session);
        }
    }

    private String coordMaker(ChessPosition pos) {

        HashMap<Integer, String> colLettersfromNums = new HashMap<>(
                Map.of(
                        0, "h",
                        1, "g",
                        2, "f",
                        3, "e",
                        4, "d",
                        5, "c",
                        6, "b",
                        7, "a"
                )
        );

        int row = pos.getRow();
        int col = pos.getColumn();

        return colLettersfromNums.get(col-1) + row;


    }

}
