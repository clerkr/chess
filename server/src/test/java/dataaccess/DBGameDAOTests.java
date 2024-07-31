package dataaccess;

import dataAccess.DBAuthDAO;
import dataAccess.DBGameDAO;
import dataAccess.DBUserDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DBGameDAOTests {

    DBAuthDAO authDAO = new DBAuthDAO();
    DBUserDAO userDAO = new DBUserDAO();
    DBGameDAO gameDAO = new DBGameDAO();

    @BeforeEach
    public void setup() {
        authDAO.clearAuths();
        userDAO.clearUsers();
        gameDAO.clearGames();
    }

    @Test
    public void listGamesPositive() {
        gameDAO.createGame("gameName1");
        gameDAO.createGame("gameName2");
        var games = gameDAO.listGames();
        Assertions.assertEquals(2, games.size());
    }

    @Test
    public void getGamePositive() {
        int gameID1 = gameDAO.createGame("gameName1");
        GameData game1 = gameDAO.getGame(gameID1);
        Assertions.assertEquals(gameID1, game1.getGameID());
    }

    @Test
    public void getGameNegative() {
        Assertions.assertNull(gameDAO.getGame(1));
    }

}
