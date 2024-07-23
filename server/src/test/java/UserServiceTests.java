import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

import java.util.HashSet;

public class UserServiceTests {

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
    @DisplayName("Register a new user")
    public void register() throws Exception {
        UserService userService = new UserService();
        RegisterUserRequest req = new RegisterUserRequest(
                "username",
                "pass1234",
                "email@email.com"
        );
        RegisterUserResult res = userService.register(req);
        Assertions.assertEquals(res.username(), userDAO.getUser(req.username()).username());
    }

    @Test
    @DisplayName("Register a new user")
    public void registerExtant() throws Exception {
        UserService userService = new UserService();
        RegisterUserRequest req = new RegisterUserRequest(
                "username",
                "pass1234",
                "email@email.com"
        );
        RegisterUserResult res = userService.register(req);
        Assertions.assertThrows(
                ExtantUserException.class,
                () -> userService.register(req),
                "Attempted to register a user already present with the same username"
        );
    }

    @Test
    public void login() throws Exception {
        UserService userService = new UserService();
        RegisterUserRequest reqReg = new RegisterUserRequest(
                "username",
                "pass1234",
                "email@email.com"
        );
        RegisterUserResult resReg = userService.register(reqReg);
        LoginRequest reqLogin = new LoginRequest(
                "username",
                "pass1234"
        );
        LoginResult resLogin = userService.login(reqLogin);
        String username_test = authDAO.getAuth(resLogin.authToken()).username();
        Assertions.assertEquals(username_test, "username");
    }

    @Test
    public void loginNoUser() throws Exception {
        UserService userService = new UserService();
        LoginRequest reqLogin = new LoginRequest(
                "username",
                "pass1234"
        );
        Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userService.login(reqLogin)
        );

    }

}
