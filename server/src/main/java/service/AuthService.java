package service;

import dataAccess.MemoryAuthDAO;

public class AuthService implements Service {

    MemoryAuthDAO authDAO = MemoryAuthDAO.getInstance();

    @Override
    public void clear() {
        authDAO.clearAuths();
    }
}
