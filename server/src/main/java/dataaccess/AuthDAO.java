package dataAccess;

import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
    void clearAuths();
    AuthData getAuth(String token);
    String createAuth(String username);
    void deleteAuth(AuthData auth);
}
