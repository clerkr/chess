package dataaccess;
import dataaccess.interfaces.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;


public class DBUserDAO implements UserDAO {

    public DBUserDAO() {
        try {
            final String[] createStatements = {
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
            DBConfigurer.configureDatabase(createStatements);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void clearUsers() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE users";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            // Swallow
        }
    }

    @Override
    public UserData getUser(String username) {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT id, username, password, email FROM users WHERE username=?";

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    String usernameFromDB = null;
                    String hashedPassword = null;
                    String email = null;

                    if (rs.next()) {
//                var id = rs.getInt("id");
                        usernameFromDB = rs.getString("username");
                        hashedPassword = rs.getString("password");
                        email = rs.getString("email");
                    }
                    if (usernameFromDB == null) {
                        throw new UserNotFoundException("No user found");
                    }
                    return new UserData(usernameFromDB, hashedPassword, email);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public String createUser(UserData user) {

        if (!(user instanceof UserData)) { return null; }

        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO users (username, password, email) VALUES(?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, user.username());
                String rawPassword = user.password();
                String encryptPass = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
                preparedStatement.setString(2, encryptPass);
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            // Swallow
        }
        return user.username();
    }
    
}
