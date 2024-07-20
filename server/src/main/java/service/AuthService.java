package service;

import dataaccess.InvalidTokenException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.ListGamesRequest;
import model.ListGamesResult;

public class AuthService implements Service {

    MemoryAuthDAO authDAO = MemoryAuthDAO.getInstance();

    @Override
    public void clear() {
        authDAO.clearAuths();
    }
}
