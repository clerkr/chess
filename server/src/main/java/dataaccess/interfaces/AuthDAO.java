package dataaccess.interfaces;

import model.AuthData;

public interface AuthDAO {
    void clearAuths();
    AuthData getAuth(String token);
    String createAuth(String username);
    void deleteAuth(AuthData auth);
}
