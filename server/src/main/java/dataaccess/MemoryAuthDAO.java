package dataaccess;
import model.AuthData;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private static MemoryAuthDAO instance;
    HashSet<AuthData> auths = new HashSet<>();

    private MemoryAuthDAO() {
        AuthData sampleAuthData = new AuthData("1a2b3c", "sample username");
        this.auths.add(sampleAuthData);
    }

    public static synchronized MemoryAuthDAO getInstance() {
        return (instance == null) ? (instance = new MemoryAuthDAO()) : instance;
    }

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

    public static String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public void createAuth(String username) {
        auths.add(new AuthData(generateToken(), username));
    }
}
