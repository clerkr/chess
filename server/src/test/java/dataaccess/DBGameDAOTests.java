package dataaccess;

import chess.ChessGame;
import dataAccess.DBAuthDAO;
import dataAccess.DBGameDAO;
import dataAccess.DBUserDAO;
import dataAccess.InvalidGameException;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.HashSet;

public class DBGameDAOTests {

    DBGameDAO gameDAO = new DBGameDAO();

    @BeforeEach
    public void setup() {
        gameDAO.clearGames();
    }
    @AfterEach
    public void breakdown() {
        gameDAO.clearGames();
    }

    @Test
    public void listGamesPositive() {
        gameDAO.createGame("gameName1");
        gameDAO.createGame("gameName2");
        var games = gameDAO.listGames();
        Assertions.assertEquals(2, games.size());
    }

    // Empty games table for (-)

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

    @Test
    public void createGamePositive() {
        int game1ID = gameDAO.createGame("gameName1");
        GameData game1 = gameDAO.getGame(game1ID);
        Assertions.assertNotNull(game1);
    }

//    @Test
//    @DisplayName("Duplicate game creation attempt")
//    public void createGameNegative() {
//        int game1ID = gameDAO.createGame("gameName1");
//        Assertions.assertThrows(Exception.class, () -> gameDAO.createGame("gameName1"));
//    }

    @Test
    public void updateGamePositive() {
        int gameID = gameDAO.createGame("gameName");
        GameData game = gameDAO.getGame(gameID);
        GameData updatedGame = new GameData(
                gameID,
                "whiteUser",
                null,
                "gameName",
                new ChessGame()
        );
        try {
            gameDAO.updateGame(updatedGame);
        } catch (Exception e) {

        }
        game = gameDAO.getGame(gameID);
        Assertions.assertEquals(updatedGame.getWhiteUsername(), game.getWhiteUsername());

    }

    @Test
    public void updateGameNegative() {
        int gameID = gameDAO.createGame("gameName");
        GameData game = gameDAO.getGame(gameID);
        GameData updatedGame = new GameData(
                888,
                "whiteUser",
                null,
                "gameName",
                new ChessGame()
        );

        Assertions.assertThrows(InvalidGameException.class, () -> gameDAO.updateGame(updatedGame));
    }

    @Test
    public void clearGames() {
        gameDAO.createGame("gameName1");
        gameDAO.createGame("gameName2");
        gameDAO.createGame("gameName3");

        gameDAO.clearGames();

        HashSet<GameData> games = gameDAO.listGames();
        Assertions.assertEquals(0, games.size());
    }


}
