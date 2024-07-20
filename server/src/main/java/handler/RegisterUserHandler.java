package handler;

import com.google.gson.Gson;
import dataaccess.ExtantUserException;
import dataaccess.InvalidTokenException;
import model.RegisterUserRequest;
import model.RegisterUserResult;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;


public class RegisterUserHandler implements Route {

    private UserService userService = new UserService();

    public RegisterUserHandler() {}

    @Override
    public Object handle(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            if (!req.requestMethod().equalsIgnoreCase("POST")) {
                throw new IllegalArgumentException("Invalid HTTP request type");
            }
            if ( !(req.body().contains("username") && req.body().contains("password") && req.body().contains("email"))) {
                throw new IllegalArgumentException("Incorrect body params given. Username, password, and email needed");
            }

            String username = req.params("username");
            String password = req.params("password");
            String email = req.params("email");

            RegisterUserRequest service_req = new RegisterUserRequest(username, password, email);
            RegisterUserResult service_res = userService.register(service_req);

            HashMap<String, String> json_res = new HashMap<>();
            json_res.put("username", service_res.username());
            json_res.put("authToken", service_res.authToken());
            return new Gson().toJson(json_res);

        } catch (IllegalArgumentException e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        } catch (ExtantUserException e) {
            res.status(403);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        }
    }
}
