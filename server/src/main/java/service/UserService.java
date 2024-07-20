package service;

import dataaccess.*;
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

    public LoginResult login(LoginRequest req) throws Exception {
        UserData user = userDAO.getUser(req.username());
        if (user == null) { throw new UserNotFoundException("No existing use by those credentials"); }
        return new LoginResult(user.username(), authDAO.createAuth(user.username()));
    }

    @Override
    public void clear() {
        userDAO.clearUsers();
    }
}
