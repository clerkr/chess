package handler;
import com.google.gson.Gson;
import dataaccess.InvalidTokenException;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.LoginRequest;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

/*
The Handler classes are intended to convert HTTP to Java.
Send the converted information from the HTTP to the Server classes
 */


public class CreateGameHandler implements Route {

    private GameService gameService = new GameService();

    private record GameName(String gameName) {}

    public CreateGameHandler() {}

    @Override
    public Object handle(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            if (!req.requestMethod().equalsIgnoreCase("POST")) {
                throw new IllegalArgumentException("Invalid HTTP request type");
            }
            if (!(req.headers().contains("Authorization") && req.body().contains("gameName"))) {
                throw new IllegalArgumentException("Incorrect body params given... authToken needed");
            }

            String authToken = req.headers("Authorization");
            String gameName = new Gson().fromJson(req.body(), GameName.class).gameName();
            CreateGameRequest serviceReq = new CreateGameRequest(authToken, gameName);
            CreateGameResult serviceRes = gameService.createGame(serviceReq);
            return new Gson().toJson(Map.of("gameID", serviceRes.gameID()));

        } catch (IllegalArgumentException e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        } catch (InvalidTokenException e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        }


    }

//    public Object handle() {
//        var body = req.body();
//        var header = req.headers();
//    }

}
