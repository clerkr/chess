package service;

import dataaccess.*;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameService implements Service {

    MemoryAuthDAO authDAO = MemoryAuthDAO.getInstance();
    MemoryGameDAO gameDAO = MemoryGameDAO.getInstance();

    public ListGamesResult listGames(ListGamesRequest req) throws InvalidTokenException {
        String authToken = req.authToken();
        AuthData authenticated = authDAO.getAuth(authToken);
        if (authenticated == null) { throw new InvalidTokenException("Unauthorized token"); }

        ArrayList<GameData> games = gameDAO.listGames();
        ListGamesResult service_res = new ListGamesResult(games);
        return service_res;
    }

    public CreateGameResult createGame(CreateGameRequest req) throws InvalidTokenException {
        AuthData authenticated = authDAO.getAuth(req.authToken());
        if (authenticated == null) { throw new InvalidTokenException("Unauthorized token"); }

        int gameID = gameDAO.createGame(req.gameName());
        return new CreateGameResult(gameID);
    }

    public void joinGame(JoinGameRequest req) throws Exception {

        AuthData auth = authDAO.getAuth(req.authToken());
        if (auth == null) { throw new InvalidTokenException("Unauthorized token"); }
        String username = auth.username();

        GameData game = gameDAO.getGame(req.gameID());
        if (game == null) { throw new InvalidGameException("Game not found"); }

        if (Objects.equals(req.playerColor(), "WHITE")) {
            if (game.getWhiteUsername() == null) {throw new PlayerColorTakenException("already taken");}
            game.setWhiteUsername(username);
        } else if (Objects.equals(req.playerColor(), "BLACK")){
            if (game.getBlackUsername() == null) {throw new PlayerColorTakenException("already taken");}
            game.setBlackUsername(username);
        }

        gameDAO.updateGame(game);
    }

    @Override
    public void clear() {
        gameDAO.clearGames();
    }
}
