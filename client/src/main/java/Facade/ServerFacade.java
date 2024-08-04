package Facade;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.UserData;
import ui.DrawGameList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ServerFacade {

    private int port;

    public ServerFacade(int port) {
        this.port = port;
    }

    public void preHelp() {
        System.out.println(
                """
                help
                register <username> <password> <email>
                login <username> <password>
                quit
                """
        );
    }

    public void postHelp() {
        System.out.println(
                """
                help
                create <game name>
                join <game number> <WHITE | BLACK>
                observer <game number>
                list - lists all games with selection numbers
                logout
                quit
                """
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
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    Map res = new Gson().fromJson(inputStreamReader, Map.class);
                    authToken = (String) res.get("authToken");
                }
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
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    Map res = new Gson().fromJson(inputStreamReader, Map.class);
                    authToken = (String) res.get("authToken");
                }
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
                    "POST",
                    "session",
                    authToken
            );
            HttpURLConnection http = httpHandler.establish();
            http.connect();
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
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                }
            } else if (statusCode == 401) {
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

            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                Map res = new Gson().fromJson(inputStreamReader, Map.class);
                List<Map<String, Object>> games = (List<Map<String, Object>>) res.get("games");
                DrawGameList.drawTable(games, facadeGames);
            }
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
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    Map res = new Gson().fromJson(inputStreamReader, Map.class);
                    System.out.println("Joined " + selectorID);
                }
            } else if (statusCode == 403) {
                System.out.println("ERROR: That color is already taken by another player in this game");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
