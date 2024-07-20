package service;

import dataaccess.InvalidTokenException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.ListGamesRequest;
import model.ListGamesResult;

import java.util.List;

public class GameService implements Service {

    MemoryAuthDAO auth = MemoryAuthDAO.getInstance();
    MemoryGameDAO gameDAO = MemoryGameDAO.getInstance();

    public ListGamesResult listGames(ListGamesRequest req) throws InvalidTokenException {
        String authToken = req.authToken();
        AuthData authenticated = auth.getAuth(authToken);
        if (authenticated == null) { throw new InvalidTokenException("Unauthorized token"); }
        return new ListGamesResult(authenticated.authToken());
    }

    @Override
    public void clear() {
        gameDAO.clearGames();
    }
}
