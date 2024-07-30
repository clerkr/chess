package dataAccess;
import model.AuthData;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;


public class DBAuthDAO implements AuthDAO {

    public DBAuthDAO() {
        try {
            configureDatabase();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void clearAuths() {
        try {
            String statement = "TRUNCATE auths";
            Connection conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            // Swallow
        }
    }

    @Override
    public AuthData getAuth(String token) {
        try {
            String statement = "SELECT id, authToken, username FROM auths WHERE authToken=?";
            Connection conn = DatabaseManager.getConnection();

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, token);
                try (var rs = preparedStatement.executeQuery()) {
//                var id = rs.getInt("id");
                    var authToken = rs.getString("authToken");
                    var username = rs.getString("username");
                    return new AuthData(authToken, username);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public String createAuth(String username) {
        try {
            String authToken = generateToken();

            String statement = "INSERT INTO auths (authToken, username) VALUES(?, ?)";
            Connection conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }

            return authToken;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void deleteAuth(AuthData auth) {
        try {
            String statement = "DELETE FROM auths WHERE authToken=?";
            Connection conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auths (
              `id` int NOT NULL AUTO_INCREMENT,
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

}
