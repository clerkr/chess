package handler;
import com.google.gson.Gson;
import dataaccess.InvalidGameException;
import dataaccess.InvalidTokenException;
import dataaccess.MemoryAuthDAO;
import dataaccess.PlayerColorTakenException;
import model.AuthData;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.JoinGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

/*
The Handler classes are intended to convert HTTP to Java.
Send the converted information from the HTTP to the Server classes
 */


public class JoinGameHandler implements Route {

    private GameService gameService = new GameService();
    private MemoryAuthDAO auth = MemoryAuthDAO.getInstance();

    private record conversionHelper(String playerColor, int gameID) {}

    public JoinGameHandler() {}

    @Override
    public Object handle(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            if (!req.requestMethod().equalsIgnoreCase("PUT")) {
                throw new IllegalArgumentException("Invalid HTTP request type");
            }
            if (!(req.headers().contains("Authorization") && req.body().contains("gameID") && req.body().contains("playerColor"))) {
                throw new IllegalArgumentException("Incorrect body params given... authToken needed");
            }

            String authToken = req.headers("Authorization");
            AuthData authData = auth.getAuth(authToken);
            if (authData == null) { throw new InvalidTokenException("Unauthroized"); }
            String username = authData.username();

            conversionHelper body = new Gson().fromJson(req.body(), conversionHelper.class);
            JoinGameRequest serviceReq = new JoinGameRequest(authToken, username, body.playerColor(), body.gameID());
            gameService.joinGame(serviceReq);
            return new Gson().toJson(Map.of("gameID", serviceReq.gameID()));
            
        } catch (IllegalArgumentException | InvalidGameException e) {
            res.status(400);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        } catch (InvalidTokenException e) {
            res.status(401);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        } catch (PlayerColorTakenException e) {
            res.status(403);
            return new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        }


    }

//    public Object handle() {
//        var body = req.body();
//        var header = req.headers();
//    }

}
