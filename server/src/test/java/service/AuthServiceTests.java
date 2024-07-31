package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthServiceTests {

//    MemoryAuthDAO authDAO = MemoryAuthDAO.getInstance();
//    MemoryUserDAO userDAO = MemoryUserDAO.getInstance();
//    MemoryGameDAO gameDAO = MemoryGameDAO.getInstance();

    DBAuthDAO authDAO = new DBAuthDAO();
    DBUserDAO userDAO = new DBUserDAO();
    DBGameDAO gameDAO = new DBGameDAO();

    @BeforeEach
    public void setup() {
        authDAO.clearAuths();
        userDAO.clearUsers();
        gameDAO.clearGames();
    }

    @Test
    public void clear() {
        AuthService authService = new AuthService();
        String username = "SampleUsername";
        String token = authDAO.createAuth(username);
        authService.clear();
        Assertions.assertNull(authDAO.getAuth("InvalidToken"));
    }
}
