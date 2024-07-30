package dataAccess;

import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    private static MemoryUserDAO instance;
    private HashSet<UserData> users = new HashSet<>();

    private MemoryUserDAO() {}

    public static synchronized MemoryUserDAO getInstance() {
        return (instance == null) ? (instance = new MemoryUserDAO()) : instance;
    }

    @Override
    public UserData getUser(String username) {
        for (UserData user : users) {
            if (Objects.equals(user.username(), username)) { return user; }
        }
        return null;
    }

    @Override
    public void createUser(UserData user) { users.add(user); }

    @Override
    public void clearUsers() { users.clear(); }
}
