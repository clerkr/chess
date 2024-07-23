package handler;

import dataaccess.InvalidTokenException;
import model.GameData;
import model.ListGamesResult;
import service.*;
import com.google.gson.Gson;
import model.ListGamesRequest;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Array;
import java.util.*;


public class ListGamesHandler implements Route {

    private GameService gameService = new GameService();
    private record displayGameData(int gameID, String whiteUsername, String blackUsername, String gameName) {}

    public ListGamesHandler() {}

    @Override
    public Object handle(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            if (!req.requestMethod().equalsIgnoreCase("GET")) {
                throw new IllegalArgumentException("Invalid HTTP request type");
            }
            if (!req.headers().contains("Authorization")) {
                throw new IllegalArgumentException("Authorization header not found");
            }
            String authToken = req.headers("Authorization");
            ListGamesRequest serviceReq = new ListGamesRequest(authToken);
            ListGamesResult serviceRes = gameService.listGames(serviceReq);

            ArrayList<displayGameData> displayGames = new ArrayList<>();
            for (GameData game : serviceRes.games()) {
                displayGames.add(new displayGameData(
                        game.getGameID(),
                        game.getWhiteUsername(),
                        game.getBlackUsername(),
                        game.getGameName()));
            }
            return new Gson().toJson(Map.of("games", displayGames));
        } catch (IllegalArgumentException | InvalidTokenException e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        }
    }
}
