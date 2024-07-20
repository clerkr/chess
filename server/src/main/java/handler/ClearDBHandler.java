package handler;

import com.google.gson.Gson;
import dataaccess.InvalidTokenException;
import model.ListGamesRequest;
import model.ListGamesResult;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ClearDBHandler implements Route {

    private UserService userService = new UserService();
    private GameService gameService = new GameService();
    private AuthService authService = new AuthService();

    public ClearDBHandler() {}

    @Override
    public Object handle(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            if (!req.requestMethod().equalsIgnoreCase("DELETE")) {
                throw new IllegalArgumentException("Invalid HTTP request type");
            }

            authService.clear();
            userService.clear();
            gameService.clear();

            return new Gson().toJson(Collections.emptyMap());

        } catch (IllegalArgumentException e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        }
    }
}
