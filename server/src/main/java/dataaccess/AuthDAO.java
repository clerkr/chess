package dataAccess;

import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
    void clearAuths() throws Exception;
    AuthData getAuth(String token) throws Exception;
    String createAuth(String username) throws Exception;
    void deleteAuth(AuthData auth) throws Exception;
}
