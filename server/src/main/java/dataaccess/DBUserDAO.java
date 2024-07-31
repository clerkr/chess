package dataAccess;
import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;


public class DBUserDAO implements UserDAO {

    public DBUserDAO() {
        try {
            configureDatabase();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void clearUsers() {
        try {
            String statement = "TRUNCATE users";
            Connection conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            // Swallow
        }
    }

    @Override
    public UserData getUser(String username) {
        try {
            String statement = "SELECT id, username, password, email FROM users WHERE username=?";
            Connection conn = DatabaseManager.getConnection();

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    String usernameFromDB = null;
                    String password = null;
                    String email = null;

                    if (rs.next()) {
//                var id = rs.getInt("id");
                        usernameFromDB = rs.getString("username");
                        password = rs.getString("password");
                        email = rs.getString("email");
                    }
                    if (usernameFromDB == null) {
                        throw new UserNotFoundException("No user found");
                    }
                    return new UserData(usernameFromDB, password, email);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void createUser(UserData user) {

        try {
            String statement = "INSERT INTO users (username, password, email) VALUES(?, ?, ?)";
            Connection conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.password());
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            // Swallow
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
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
