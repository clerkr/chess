package handler;

import com.google.gson.Gson;
import dataaccess.UserNotFoundException;
import model.LoginRequest;
import model.LoginResult;
import model.LogoutRequest;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

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
            if ( !(req.body().contains("authorization")) ) {
                throw new IllegalArgumentException("Incorrect body params given... authToken needed");
            }

            LogoutRequest service_req = new LogoutRequest(req.headers("Authorization"));
            userService.logout(service_req);

            return new Gson().toJson("{}");

        } catch (IllegalArgumentException e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        }
    }
}
