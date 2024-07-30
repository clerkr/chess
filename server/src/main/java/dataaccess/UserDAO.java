package dataAccess;
import model.UserData;

public interface UserDAO {
    UserData getUser(String username) throws Exception;
    void createUser(UserData user) throws Exception;
    void clearUsers() throws Exception;
}
