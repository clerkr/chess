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
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM games WHERE id=?";
        Connection conn = DatabaseManager.getConnection();

        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, String.valueOf(gameID));
            try (var rs = preparedStatement.executeQuery()) {
                var id = rs.getInt("id");
                var whiteUsername = rs.getString("whiteUsername");
                var blackUsername = rs.getString("blackUsername");
                var gameName = rs.getString("gameName");
                var gameJson = rs.getString("game");
                var game = new Gson().fromJson(gameJson, ChessGame.class);
                return new GameData(id, whiteUsername, blackUsername, gameName, game);
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public int createGame(String gameName) throws SQLException, DataAccessException {

        String statement = "INSERT INTO auths (whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?, ?)";
        Connection conn = DatabaseManager.getConnection();
        int gameID;
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, null);
            preparedStatement.setString(2, null);
            preparedStatement.setString(3, gameName);
            String gameJson = new Gson().toJson(new ChessGame());
            preparedStatement.setString(4, gameJson);

            preparedStatement.executeUpdate();

            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    gameID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating game failed, no ID obtained.");
                }
            }
        }
        return gameID;

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
