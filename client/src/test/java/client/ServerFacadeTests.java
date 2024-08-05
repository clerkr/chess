package client;

import Facade.FacadeGameData;
import Facade.ServerFacade;
import dataaccess.DBAuthDAO;
import dataaccess.DBGameDAO;
import dataaccess.DBUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.HashSet;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static DBAuthDAO authDAO;
    private static DBUserDAO userDAO;
    private static DBGameDAO gameDAO;

    @BeforeAll
    public static void init() {
        server = new Server();
        authDAO = new DBAuthDAO();
        userDAO = new DBUserDAO();
        gameDAO = new DBGameDAO();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterEach
    void cleardb() {
        authDAO.clearAuths();
        userDAO.clearUsers();
        gameDAO.clearGames();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }




    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Clean new user register")
    public void register() {
        String username = "username1";
        UserData newUser = new UserData(username, "password1234", "user@email.com");
        String newAuthToken = facade.register(newUser);
        Assertions.assertNotEquals("", authDAO.getAuth(newAuthToken));
    }

    @Test
    @DisplayName("Attempt to register under an existing username")
    public void registerNeg() {
        UserData newUser = new UserData("username1", "password1234", "user@email.com");
        String authToken = facade.register(newUser);
        String dupe = facade.register(newUser);
        Assertions.assertEquals("", dupe);
    }

    @Test
    @DisplayName("Clean login of existing user")
    public void login() {
        String username = "username1";
        String password = "password1234";
        UserData newUser = new UserData(username, password, "user@email.com");
        String authToken = facade.register(newUser);
        String loginAuthToken = facade.login(username, password);
        Assertions.assertNotEquals("", loginAuthToken);
    }

    @Test
    @DisplayName("Login attempt of nonexistent user")
    public void loginNeg() {
        String username = "username2";
        String password = "password1234";
        String loginAuthToken = facade.login(username, password);
        Assertions.assertEquals("", loginAuthToken);
    }

    @Test
    @DisplayName("Clean logout of existing user")
    public void logout() {
        String username = "username2";
        String password = "password3456";
        UserData newUser = new UserData(username, password, "user@xmail.com");
        String authToken = facade.register(newUser);
        UserData loginUser = userDAO.getUser(username);
        if (loginUser == null) { Assertions.fail(); }
        facade.logout(authToken);
        Assertions.assertNull(authDAO.getAuth(authToken));
    }

    @Test
    @DisplayName("Logout attempt on auth that is not present in db table")
    public void logoutNeg() {
        String authToken = "fakeAuthToken";
        int preLogoutTotal = authDAO.countAuths();
        facade.logout(authToken);
        int postLogoutTotal = authDAO.countAuths();
        Assertions.assertEquals(preLogoutTotal, postLogoutTotal);
    }

    @Test
    @DisplayName("Clean create game attempt")
    public void createGame() {
        String username = "username3";
        String password = "password4567";
        UserData newUser = new UserData(username, password, "user@xmail.com");
        String authToken = facade.register(newUser);
        int preCreateGame = gameDAO.countGames();
        facade.createGame(authToken, "game1");
        int postCreateGame = gameDAO.countGames();
        Assertions.assertEquals(postCreateGame, preCreateGame + 1);
    }

    @Test
    @DisplayName("Create game attempt with bad auth token")
    public void createGameNeg() {
        int preCreateGame = gameDAO.countGames();
        facade.createGame("invalidAuthToken", "game1");
        int postCreateGame = gameDAO.countGames();
        Assertions.assertEquals(postCreateGame, preCreateGame);
    }

    @Test
    @DisplayName("Clean list game attempt")
    public void listGames() {
        String username = "username3";
        String password = "password4567";
        UserData newUser = new UserData(username, password, "user@xmail.com");
        String authToken = facade.register(newUser);
        facade.createGame(authToken, "game1");
        facade.createGame(authToken, "game2");
        facade.createGame(authToken, "game3");
        HashSet<FacadeGameData> games = facade.listGames(authToken);
        Assertions.assertEquals(3, games.size());
    }

    @Test
    @DisplayName("Empty list game attempt")
    public void listGamesNeg() {
        String username = "username4";
        String password = "password1234";
        UserData newUser = new UserData(username, password, "user@xmail.com");
        String authToken = facade.register(newUser);
        HashSet<FacadeGameData> games = facade.listGames(authToken);
        Assertions.assertEquals(0, games.size());
    }




}

