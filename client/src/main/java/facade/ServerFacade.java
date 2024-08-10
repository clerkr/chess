package facade;

import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.ResponseException;
import execution.ClientExecution;
import model.UserData;
import ui.DrawGameList;
import ui.GameUI;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ServerFacade {

    private int port;

    GameUI gameUI = new GameUI();
    private WebSocketFacade wsf = new WebSocketFacade(gameUI, ClientExecution.PORT);

    public ServerFacade(int port) {
        this.port = port;
    }

    public void preHelp() {
        System.out.println(
                """
                register <username> <password> <email> - to create a new user
                login <username> <password>
                quit - exit program
                help - present commands
                """
        );
    }

    public void postHelp() {
        System.out.println(
                """
                create <game name> - starts a new game
                join <game number> <WHITE | BLACK> - participate in a game by color
                observe <game number> - for game spectation
                list - lists all games with selection numbers
                logout
                quit - exit program
                help - present commands"""
        );
    }

    public String register(UserData newUser) {
        String authToken = "";
        try {
            var body = Map.of(
                    "username", newUser.username(),
                    "password", newUser.password(),
                    "email", newUser.email());
            HttpHandler httpHandler = new HttpHandler(
                    port,
                    "POST",
                    "user",
                    authToken,
                    body
            );
            HttpURLConnection http = httpHandler.establish();
            httpHandler.runOutputStream(http);

            int statusCode = http.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                Map res = httpHandler.runInputStream(http);
                authToken = (String) res.get("authToken");
            } else if (statusCode == 403) {
                System.out.println("ERROR: Username already in use");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return authToken;
    }

    public String login(String username, String password) {
        String authToken = "";
        try {
            var body = Map.of(
                    "username", username,
                    "password", password
            );
            HttpHandler httpHandler = new HttpHandler(
                    port,
                    "POST",
                    "session",
                    authToken,
                    body
            );
            HttpURLConnection http = httpHandler.establish();
            httpHandler.runOutputStream(http);

            int statusCode = http.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                Map res = httpHandler.runInputStream(http);
                authToken = (String) res.get("authToken");

            } else if (statusCode == 401) {
                System.out.println("ERROR: Invalid username or password");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return authToken;
    }

    public void quit() {
        System.out.println("Closing chess 240...");
        System.exit(0);
    }

    public void logout(String authToken) {
        try {
            HttpHandler httpHandler = new HttpHandler(
                    port,
                    "DELETE",
                    "session",
                    authToken
            );
            HttpURLConnection http = httpHandler.establish();
            http.connect();
            int responseCode = http.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createGame(String authToken, String gameName) {
        try {
            var body = Map.of(
                    "gameName", gameName
            );
            HttpHandler httpHandler = new HttpHandler(
                    port,
                    "POST",
                    "game",
                    authToken,
                    body
            );
            HttpURLConnection http = httpHandler.establish();
            httpHandler.runOutputStream(http);

            int statusCode = http.getResponseCode();
            if (statusCode == 401) {
                System.out.println("ERROR: Could not create game");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashSet<FacadeGameData> listGames(String authToken) {
        HashSet<FacadeGameData> facadeGames = new HashSet<FacadeGameData>();
        try {
            HttpHandler httpHandler = new HttpHandler(
                    port,
                    "GET",
                    "game",
                    authToken
            );
            HttpURLConnection http = httpHandler.establish();

            int statusCode = http.getResponseCode();
            if (statusCode == 401) {
                System.out.println("ERROR: Could not create game");
            }
            if (statusCode != HttpURLConnection.HTTP_OK) { return facadeGames; }

            Map res = httpHandler.runInputStream(http);
            List<Map<String, Object>> games = (List<Map<String, Object>>) res.get("games");
            DrawGameList.drawTable(games, facadeGames);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return facadeGames;
    }

    public void joinGame(int gameID, int selectorID, String authToken, String playerColor) {
        try {
            var body = Map.of(
                    "gameID", String.valueOf(gameID),
                    "playerColor", playerColor.toUpperCase()
            );
            HttpHandler httpHandler = new HttpHandler(
                    port,
                    "PUT",
                    "game",
                    authToken,
                    body
            );
            HttpURLConnection http = httpHandler.establish();
            httpHandler.runOutputStream(http);

            int statusCode = http.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                    Map res = httpHandler.runInputStream(http);
                    try {
                        wsf.connectSender(authToken, gameID);
                    } catch (ResponseException e) {
                        throw new RuntimeException(e);
                    }
            } else if (statusCode == 403) {
                System.out.println("ERROR: That color is already taken by another player in this game");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void observeGame(int gameID, int selectorID, String authToken) {
        try {
            wsf.connectSender(authToken, gameID);
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawBoardHandler() {
        wsf.drawGameUIBoardHandler();
    }

    public boolean checkValidMove(ChessMove move) {
        return gameUI.checkValidMove(move);
    }

    public boolean checkPlayersTurn() {
        return gameUI.checkPlayersTurn();
    }

    public boolean checkPiecePlayersColor(ChessMove move) {
        return gameUI.checkPiecePlayersColor(move);
    }

    public void sendMakeMoveHandler(String authToken, int gameID, ChessMove move) {
        wsf.makeMoveSender(authToken, gameID, move);
    }
}
