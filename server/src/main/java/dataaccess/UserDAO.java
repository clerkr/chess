package dataAccess;
import model.UserData;

public interface UserDAO {
    UserData getUser(String username);
    void createUser(UserData user);
    void clearUsers();
}
