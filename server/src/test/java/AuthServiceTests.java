import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

public class AuthServiceTests {

    MemoryAuthDAO authDAO = MemoryAuthDAO.getInstance();
    MemoryUserDAO userDAO = MemoryUserDAO.getInstance();
    MemoryGameDAO gameDAO = MemoryGameDAO.getInstance();

    @BeforeEach
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

    @Test
    public void logout() throws Exception {
        UserService userService = new UserService();
        RegisterUserRequest reqReg = new RegisterUserRequest(
                "username",
                "pass1234",
                "email@email.com"
        );
        RegisterUserResult resReg = userService.register(reqReg);
        LogoutRequest reqLogout = new LogoutRequest(resReg.authToken());
        userService.logout(reqLogout);
        Assertions.assertEquals(null, authDAO.getAuth(resReg.authToken()));
    }

    @Test
    public void logoutNoUser() throws Exception {
        UserService userService = new UserService();
        RegisterUserRequest reqReg = new RegisterUserRequest(
                "username",
                "pass1234",
                "email@email.com"
        );
        RegisterUserResult resReg = userService.register(reqReg);
        LogoutRequest reqLogout = new LogoutRequest("invalidAuthToken");
        Assertions.assertThrows(
                InvalidTokenException.class,
                () -> userService.logout(reqLogout)
        );
    }

    @Test
    public void clearUsers() throws Exception {
        UserService userService = new UserService();
        RegisterUserRequest reqReg1 = new RegisterUserRequest(
                "username",
                "pass1234",
                "email@email.com"
        );
        RegisterUserRequest reqReg2 = new RegisterUserRequest(
                "username2",
                "pass1234",
                "email@email.com"
        );
        RegisterUserResult resReg1 = userService.register(reqReg1);
        RegisterUserResult resReg2 = userService.register(reqReg2);
        userService.clear();

        boolean checker = userDAO.getUser(reqReg1.username()) == null && userDAO.getUser(reqReg1.username()) == null;
        Assertions.assertTrue(checker);
    }

}
