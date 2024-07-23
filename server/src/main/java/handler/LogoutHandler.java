package handler;

import com.google.gson.Gson;
import dataaccess.InvalidTokenException;
import dataaccess.UserNotFoundException;
import model.LoginRequest;
import model.LoginResult;
import model.LogoutRequest;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class LogoutHandler implements Route {

    private UserService userService = new UserService();

    public LogoutHandler() {}

    @Override
    public Object handle(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            if (!req.requestMethod().equalsIgnoreCase("DELETE")) {
                throw new IllegalArgumentException("Invalid HTTP request type");
            }
            if ( !(req.headers().contains("Authorization")) ) {
                throw new IllegalArgumentException("Incorrect body params given... authToken needed");
            }

            LogoutRequest serviceReq = new LogoutRequest(req.headers("Authorization"));
            userService.logout(serviceReq);

            return new Gson().toJson(Collections.emptyMap());

        } catch (IllegalArgumentException e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        } catch (InvalidTokenException e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        }
    }
}
