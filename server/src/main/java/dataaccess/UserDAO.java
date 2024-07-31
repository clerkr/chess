package dataAccess;
import model.UserData;

public interface UserDAO {
    UserData getUser(String username);
    String createUser(UserData user);
    void clearUsers();
}
