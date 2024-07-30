package dataAccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;


public class DBGameDAO implements GameDAO {

    public DBGameDAO() throws Exception {
        configureDatabase();
    }

    @Override
    public HashSet<GameData> listGames() throws SQLException, DataAccessException {
        HashSet<GameData> games = new HashSet<>();
        String statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM games";
        Connection conn = DatabaseManager.getConnection();

        try (var preparedStatement = conn.prepareStatement(statement)) {
            try (var rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getInt("id");
                    var whiteUsername = rs.getString("whiteUsername");
                    var blackUsername = rs.getString("blackUsername");
                    var gameName = rs.getString("gameName");
                    var gameJson = rs.getString("game");
                    var game = new Gson().fromJson(gameJson, ChessGame.class);

                    games.add(new GameData(id, whiteUsername, blackUsername, gameName, game));
                }
            }
        }
        return games;
    }

    @Override
    public void clearAuths() throws DataAccessException, SQLException {
        String statement = "TRUNCATE auths";
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException, SQLException{
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
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public String createAuth(String username) throws DataAccessException, SQLException{

        String statement = "INSERT INTO auths (authToken, username) VALUES(?, ?)";
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        }

        return authToken;
    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException, SQLException {
        String statement = "DELETE FROM auths WHERE authToken=?";
        Connection conn = DatabaseManager.getConnection();
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, auth.authToken());
            preparedStatement.executeUpdate();
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
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
