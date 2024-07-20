package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuth(String token) throws InvalidTokenException;
    void createAuth(String username);
}
