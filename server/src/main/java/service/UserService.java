package service;

import dataaccess.ExtantUserException;
import dataaccess.InvalidTokenException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.*;

public class UserService implements Service {

    MemoryUserDAO userDAO = MemoryUserDAO.getInstance();

    public RegisterUserResult register(RegisterUserRequest req) throws Exception {

        if (userDAO.getUser(req.username()) != null) { throw new ExtantUserException("User already present"); }

        UserData newUser = new UserData(req.username(), req.password(), req.email());


    }

    @Override
    public void clear() {
        userDAO.clearUsers();
    }
}
