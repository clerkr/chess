package handler;

import dataaccess.InvalidTokenException;
import model.ListGamesResult;
import service.*;
import com.google.gson.Gson;
import model.ListGamesRequest;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;


public class ListGamesHandler implements Route {

    private GameService gameService = new GameService();

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
            ListGamesRequest service_req = new ListGamesRequest(authToken);

            ListGamesResult service_res = gameService.listGames(service_req);

            String service_rep_token = service_res.authToken();

            HashMap<String, String> authTokenMap = new HashMap<>();
            authTokenMap.put("authorization", service_rep_token);
            return new Gson().toJson(authTokenMap);

        } catch (IllegalArgumentException | InvalidTokenException e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        }
    }
}
