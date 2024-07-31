package service;

import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserService implements Service {

    DBAuthDAO authDAO = new DBAuthDAO(); // MemoryAuthDAO.getInstance();
    DBUserDAO userDAO = new DBUserDAO(); // MemoryUserDAO.getInstance();

    public UserService() {
    }

    public RegisterUserResult register(RegisterUserRequest req) throws Exception {

        if (userDAO.getUser(req.username()) != null) { throw new ExtantUserException("User already present"); }

        UserData newUser = new UserData(req.username(), req.password(), req.email());
        userDAO.createUser(newUser);
        String authToken = authDAO.createAuth(req.username());

        return new RegisterUserResult(req.username(), authToken);
    }

    public LoginResult login(LoginRequest req) throws Exception {
        UserData user = userDAO.getUser(req.username());
        if (user == null) { throw new UserNotFoundException("No existing user from credentials"); }
//        if (!Objects.equals(user.password(), req.password())) { throw new InvalidPasswordException("unauthorized"); }
        if (!BCrypt.checkpw(req.password(), user.password())) { throw new InvalidPasswordException("unauthorized"); }
        return new LoginResult(user.username(), authDAO.createAuth(user.username()));
    }

    public void logout(LogoutRequest req) throws Exception {
        AuthData authData = authDAO.getAuth(req.authToken());
        if (authData == null) { throw new InvalidTokenException("unauthorized"); }
        authDAO.deleteAuth(authData);
    }

    @Override
    public void clear() {
        try {
            userDAO.clearUsers();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
