package handler;

import com.google.gson.Gson;
import dataaccess.ExtantUserException;
import dataaccess.InvalidPasswordException;
import dataaccess.UserNotFoundException;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterUserRequest;
import model.RegisterUserResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;


public class LoginHandler implements Route {

    private UserService userService = new UserService();

    public LoginHandler() {}

    @Override
    public Object handle(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            if (!req.requestMethod().equalsIgnoreCase("POST")) {
                throw new IllegalArgumentException("Invalid HTTP request type");
            }
            if ( !(req.body().contains("username") && req.body().contains("password"))) {
                throw new IllegalArgumentException("Incorrect body params given. Username and password needed");
            }

            LoginRequest service_req = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResult service_res = userService.login(service_req);

            HashMap<String, String> json_res = new HashMap<>();
            json_res.put("username", service_res.username());
            json_res.put("authToken", service_res.authToken());
            return new Gson().toJson(json_res);

        } catch (IllegalArgumentException e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        } catch (UserNotFoundException | InvalidPasswordException e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        }
    }
}
