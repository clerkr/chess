import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.util.HashSet;
import java.util.List;

public class GameServiceTests {

    MemoryAuthDAO authDAO = MemoryAuthDAO.getInstance();
    MemoryUserDAO userDAO = MemoryUserDAO.getInstance();
    MemoryGameDAO gameDAO = MemoryGameDAO.getInstance();

    @AfterEach
    public void setup() {
        authDAO.clearAuths();
        userDAO.clearUsers();
        gameDAO.clearGames();
    }

    @Test
    @DisplayName("List games: empty")
    public void testListGamesEmpty() throws InvalidTokenException {

        String testUsername = "user123";
        String testAuthToken = authDAO.createAuth(testUsername);
        GameService gameService = new GameService();

        HashSet<GameData> games = new HashSet<>();
        ListGamesResult resTest = new ListGamesResult(games);
        ListGamesResult res = gameService.listGames(new ListGamesRequest(testAuthToken));
        Assertions.assertEquals(res.games(), resTest.games());

    }

    @Test
    @DisplayName("List of 2 games")
    public void testListGames() throws InvalidTokenException {

        String testUsername = "user123";
        String testAuthToken = authDAO.createAuth(testUsername);
        GameService gameService = new GameService();

        HashSet<GameData> games = new HashSet<>();
        GameData game1 = new GameData(
                1,
                null,
                null,
                "game1",
                new ChessGame());
        GameData game2 = new GameData(
                2,
                null,
                null,
                "game2",
                new ChessGame());
        games.add(game1);
        games.add(game2);
        gameService.createGame(new CreateGameRequest(testAuthToken, "game1"));
        gameService.createGame(new CreateGameRequest(testAuthToken, "game2"));

        ListGamesResult resTest = new ListGamesResult(games);
        ListGamesResult res = gameService.listGames(new ListGamesRequest(testAuthToken));

        Assertions.assertEquals(res.games(), resTest.games());
    }

    @Test
    @DisplayName("List games with invalid token")
    public void testListGamesInvalidAuth() throws InvalidTokenException {

        String testUsername = "user123";
        String testAuthToken = authDAO.createAuth(testUsername);
        GameService gameService = new GameService();

        HashSet<GameData> games = new HashSet<>();
        GameData game1 = new GameData(
                1,
                null,
                null,
                "game1",
                new ChessGame());
        GameData game2 = new GameData(
                2,
                null,
                null,
                "game2",
                new ChessGame());
        games.add(game1);
        games.add(game2);
        gameService.createGame(new CreateGameRequest(testAuthToken, "game1"));
        gameService.createGame(new CreateGameRequest(testAuthToken, "game2"));

        ListGamesResult resTest = new ListGamesResult(games);
        Assertions.assertThrows(
                InvalidTokenException.class,
                () -> gameService.listGames(new ListGamesRequest("invalidToken")),
                "Invalid auth token"
                );
    }

    @Test
    @DisplayName("Create new game with invalid auth")
    public void createNewGameInvalidAuth() throws Exception{
        GameService gameService = new GameService();
        GameData game1test = new GameData(
                1,
                null,
                null,
                "game1",
                new ChessGame());
        CreateGameRequest req = new CreateGameRequest("invalid auth", "game1");
        Assertions.assertThrows(
                InvalidTokenException.class,
                () -> gameService.createGame(req),
                "Invalid auth token used"
        );

    }

    @Test
    @DisplayName("Create new game")
    public void createNewGame() throws Exception{
        GameService gameService = new GameService();
        GameData game1test = new GameData(
                1,
                null,
                null,
                "game1",
                new ChessGame());
        gameDAO.createGame("game1");
        GameData game1 = gameDAO.getGame(1);
        Assertions.assertEquals(game1test, game1);
    }


    @Test
    @DisplayName("User joining a game")
    public void joinGame() throws Exception{
        String testUsername1 = "username1";
        String testUsername2 = "username2";
        String testAuthToken1 = authDAO.createAuth(testUsername1);
        String testAuthToken2 = authDAO.createAuth(testUsername2);
        GameService gameService = new GameService();
        GameData game1test = new GameData(
                1,
                testUsername1,
                testUsername2,
                "game1",
                new ChessGame());
        gameDAO.createGame("game1");
        gameService.joinGame(new JoinGameRequest(testAuthToken1, testUsername1, "WHITE", 1));
        gameService.joinGame(new JoinGameRequest(testAuthToken2, testUsername2, "BLACK", 1));
        GameData game1 = gameDAO.getGame(1);
        Assertions.assertEquals(game1test, game1);
    }

    @Test
    @DisplayName("Create new game")
    public void joinGameOccupiedColor() throws Exception{
        String testUsername1 = "username1";
        String testUsername2 = "username2";
        String testAuthToken1 = authDAO.createAuth(testUsername1);
        String testAuthToken2 = authDAO.createAuth(testUsername2);
        GameService gameService = new GameService();
        GameData game1test = new GameData(
                1,
                testUsername2,
                null,
                "game1",
                new ChessGame());
        gameDAO.createGame("game1");
        gameService.joinGame(new JoinGameRequest(testAuthToken1, testUsername1, "WHITE", 1));
        Assertions.assertThrows(
                PlayerColorTakenException.class,
                () -> gameService.joinGame(new JoinGameRequest(testAuthToken2, testUsername1, "WHITE", 1)),
                "Should throw PlayerColorTakenException when WHITE is already taken"
        );

    }

    @Test
    @DisplayName("Clear games")
    public void clearGames() throws InvalidTokenException {
        String testUsername = "user123";
        String testAuthToken = authDAO.createAuth(testUsername);
        GameService gameService = new GameService();

        HashSet<GameData> games = new HashSet<>();

        gameService.createGame(new CreateGameRequest(testAuthToken, "game1"));
        gameService.createGame(new CreateGameRequest(testAuthToken, "game2"));
        gameService.clear();
        ListGamesResult resTest = new ListGamesResult(games);
        Assertions.assertEquals(games, resTest.games());
    }

    @Test
    public void clearGamesNegative() {

    }

}
