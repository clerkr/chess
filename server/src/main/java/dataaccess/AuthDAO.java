package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clearAuths();
    AuthData getAuth(String token) throws InvalidTokenException;
    String createAuth(String username);
    void deleteAuth(AuthData auth);
}
