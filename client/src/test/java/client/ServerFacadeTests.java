package client;

import Facade.ServerFacade;
import dataaccess.DBAuthDAO;
import dataaccess.DBGameDAO;
import dataaccess.DBUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;


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
}
