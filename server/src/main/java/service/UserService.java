package service;

import dataaccess.ExtantUserException;
import dataaccess.InvalidTokenException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.*;

public class UserService implements Service {

    MemoryAuthDAO authDAO = MemoryAuthDAO.getInstance();
    MemoryUserDAO userDAO = MemoryUserDAO.getInstance();

    public RegisterUserResult register(RegisterUserRequest req) throws Exception {

        if (userDAO.getUser(req.username()) != null) { throw new ExtantUserException("User already present"); }

        UserData newUser = new UserData(req.username(), req.password(), req.email());
        userDAO.createUser(newUser);
        String authToken = authDAO.createAuth(req.username());

        return new RegisterUserResult(req.username(), authToken);
    }

    @Override
    public void clear() {
        userDAO.clearUsers();
    }
}
