package handler;

import com.google.gson.Gson;
import dataAccess.ExtantUserException;
import model.RegisterUserRequest;
import model.RegisterUserResult;
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

            RegisterUserRequest serviceReq = new Gson().fromJson(req.body(), RegisterUserRequest.class);
            RegisterUserResult serviceRes = userService.register(serviceReq);

            HashMap<String, String> jsonRes = new HashMap<>();
            jsonRes.put("username", serviceRes.username());
            jsonRes.put("authToken", serviceRes.authToken());
            return new Gson().toJson(jsonRes);

        } catch (IllegalArgumentException e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        } catch (ExtantUserException e) {
            res.status(403);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        }
    }
}
