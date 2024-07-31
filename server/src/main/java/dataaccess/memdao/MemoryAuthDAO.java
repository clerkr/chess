package dataaccess.memdao;
import dataaccess.interfaces.AuthDAO;
import model.AuthData;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private static MemoryAuthDAO instance;
    private HashSet<AuthData> auths = new HashSet<>();

    private MemoryAuthDAO() {
    }

    public static synchronized MemoryAuthDAO getInstance() {
        return (instance == null) ? (instance = new MemoryAuthDAO()) : instance;
    }

    @Override
    public void clearAuths() { auths.clear(); }

    @Override
    public AuthData getAuth(String token) {
        for (AuthData auth : auths) {
            if (Objects.equals(auth.authToken(), token)) {
                return auth;
            }
        }
        return null;
    }

    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public String createAuth(String username) {
        String authToken = generateToken();
        auths.add(new AuthData(authToken, username));
        return authToken;
    }

    @Override
    public void deleteAuth(AuthData auth) {
        auths.remove(auth);
    }
}
