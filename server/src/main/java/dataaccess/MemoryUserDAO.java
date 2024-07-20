package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    private static MemoryUserDAO instance;
    private HashSet<UserData> users = new HashSet<>();

    private MemoryUserDAO() {}

    public static synchronized MemoryUserDAO getInstance() {
        return (instance == null) ? (instance = new MemoryUserDAO()) : instance;
    }

    public UserData getUser(String username) {
        for (UserData user : users) {
            if (Objects.equals(user.username(), username)) { return user; }
        }
        return null;
    }

    public void createUser(UserData user) { users.add(user); }

    public void clearUsers() { users.clear(); }
}
