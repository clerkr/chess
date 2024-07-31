package dataaccess;
import dataAccess.DBAuthDAO;
import dataAccess.DBUserDAO;
import dataAccess.InvalidTokenException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class DBAuthDAOTests {
    DBAuthDAO authDAO = new DBAuthDAO();

    @BeforeEach
    public void setup() { authDAO.clearAuths(); }
    @AfterEach
    public void breakdown() {
        authDAO.clearAuths();
    }

    @Test
    public void getAuthPositive() {
        String authToken = authDAO.createAuth("username");
        Assertions.assertNotNull(authDAO.getAuth(authToken));
    }

    @Test
    public void getAuthNegative() {
        String authToken = authDAO.createAuth("username");
        Assertions.assertNull(authDAO.getAuth("invalidToken"));
    }

    @Test
    public void createAuthPositive() {
        String username1 = "user1";
        String authToken1 = authDAO.createAuth(username1);
        Assertions.assertNotNull(authDAO.getAuth(authToken1));
    }

    @Test
    public void createAuthNegative() {
        Assertions.assertNull(authDAO.createAuth(null));
    }

    @Test
    public void deleteAuthPositive() {
        String username = "username1";
        String authToken1 = authDAO.createAuth(username);
        AuthData auth = authDAO.getAuth(authToken1);
        authDAO.deleteAuth(auth);
        Assertions.assertNull(authDAO.getAuth(authToken1));
    }

    @Test
    public void deleteAuthNegative() {
        AuthData auth = new AuthData("authToken1", "username1");
        String authToken1 = authDAO.createAuth(auth.username());
        AuthData shellAuth = new AuthData("authToken2", "username2");
        authDAO.deleteAuth(shellAuth);

        Assertions.assertNotNull(authDAO.getAuth(authToken1));
    }

    @Test
    public void clearAuthsTest() {
        String username = "username1";
        String authToken1 = authDAO.createAuth(username);
        authDAO.clearAuths();
        Assertions.assertNull(authDAO.getAuth(authToken1));    }

}
