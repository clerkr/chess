package dataaccess;
import dataaccess.interfaces.AuthDAO;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;


public class DBAuthDAO implements AuthDAO {

    public DBAuthDAO() {
        try {
            String[] createStatements = {
                    """
            CREATE TABLE IF NOT EXISTS auths (
              `id` int NOT NULL AUTO_INCREMENT,
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            DBConfigurer.configureDatabase(createStatements);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void clearAuths() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE auths";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            // Swallow
        }
    }

    @Override
    public AuthData getAuth(String token) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT id, authToken, username FROM auths WHERE authToken=?";

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, token);
                try (var rs = preparedStatement.executeQuery()) {
//                var id = rs.getInt("id");
                    String authToken = null;
                    String username = null;
                    if(rs.next()) {
                        authToken = rs.getString("authToken");
                        username = rs.getString("username");
                    }
                    if (authToken == null || username == null) {
                        throw new InvalidTokenException("Token not found");
                    }
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

        if (!(username instanceof String)) { return null; }

        try (Connection conn = DatabaseManager.getConnection()) {
            String authToken = generateToken();

            String statement = "INSERT INTO auths (authToken, username) VALUES(?, ?)";
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
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "DELETE FROM auths WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("FLAG" + e + e.getMessage());
        }
    }

    public int countAuths() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String query = "SELECT COUNT(*) AS total FROM auths";
            try (var preparedStatement = conn.prepareStatement(query);
                 var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total");
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Error accessing database: " + e.getMessage());
        }
        return -1;  // Return -1 to indicate an error
    }




}
