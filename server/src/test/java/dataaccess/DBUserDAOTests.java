package dataaccess;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class DBUserDAOTests {
    DBUserDAO userDAO = new DBUserDAO();

    @BeforeEach
    public void setup() {
        userDAO.clearUsers();
    }
    @AfterEach
    public void breakdown() {
        userDAO.clearUsers();
    }

    @Test
    public void getUserPositive() {
        UserData newUser1 = new UserData(
                "userName1",
                "password1",
                "email1@email.com"
        );
        userDAO.createUser(newUser1);

        UserData user1 = userDAO.getUser(newUser1.username());
        Assertions.assertTrue(BCrypt.checkpw(newUser1.password(), user1.password()));
    }

    @Test
    public void getUserNegative() {
        Assertions.assertNull(userDAO.getUser("username"));
    }

    @Test
    public void createUserPositive() {
        UserData newUser = new UserData(
                "username1",
                "password",
                "email@email.com"
        );
        userDAO.createUser(newUser);
        UserData user = userDAO.getUser(newUser.username());
        Assertions.assertNotNull(user);
    }

    @Test
    public void createUserNegative() {
        Assertions.assertNull(userDAO.createUser(null));
    }

    @Test
    public void clearUsersTest() {
        UserData newUser1 = new UserData(
                "username1",
                "password",
                "email@email.com"
        );
        userDAO.createUser(newUser1);
        userDAO.clearUsers();
        Assertions.assertNull(userDAO.getUser(newUser1.username()));
    }


}
