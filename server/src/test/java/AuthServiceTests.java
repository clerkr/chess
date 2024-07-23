import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.AuthService;
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
    public void clear() {
        AuthService authService = new AuthService();
        String username = "SampleUsername";
        String token = authDAO.createAuth(username);
        authService.clear();
        Assertions.assertNull(authDAO.getAuth("InvalidToken"));
    }
}
