package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.interfaces.GameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;


public class DBGameDAO implements GameDAO {

    public DBGameDAO() {
        try {
            final String[] createStatements = {
                    """
            CREATE TABLE IF NOT EXISTS games (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT '',
              `blackUsername` varchar(256) DEFAULT '',
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
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
    public HashSet<GameData> listGames() {
        HashSet<GameData> games = new HashSet<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM games";
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
        } catch (Exception e) {
            // Swallowing it for now
        }
        return games;
    }

    @Override
    public GameData getGame(int gameID) {
        String statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM games WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection()) {

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, String.valueOf(gameID));
                try (var rs = preparedStatement.executeQuery()) {
                    int id = -1;
                    String whiteUsername = null;
                    String blackUsername = null;
                    String gameName = null;
                    ChessGame game = null;
                    if (rs.next()) {
                        id = rs.getInt("id");
                        whiteUsername = rs.getString("whiteUsername");
                        blackUsername = rs.getString("blackUsername");
                        gameName = rs.getString("gameName");
                        var gameJson = rs.getString("game");
                        game = new Gson().fromJson(gameJson, ChessGame.class);
                    }
                    if (id == -1) { throw new InvalidGameException("No game"); }
                    return new GameData(id, whiteUsername, blackUsername, gameName, game);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public int createGame(String gameName) {

        int gameID = -1;

        if (gameName == null) { return gameID; }

        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES(?, ?, ?, ?)";

            try (var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setNull(1, Types.VARCHAR);
                preparedStatement.setNull(2, Types.VARCHAR);
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
            } catch (Exception e) {
                // Swallowing
                System.out.println(e);
            }
        } catch (Exception e) {
            // Swallowing
            System.out.println(e);
        }
        return gameID;

    }

    @Override
    public void updateGame(GameData updatedGame) throws InvalidGameException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "UPDATE games SET whiteUsername=?, blackUsername=?, gameName=? WHERE id=?";

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, updatedGame.getWhiteUsername());
                preparedStatement.setString(2, updatedGame.getBlackUsername());
                preparedStatement.setString(3, updatedGame.getGameName());
                preparedStatement.setInt(4, updatedGame.getGameID());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new InvalidGameException("No game found by that ID");
                }
            }
        } catch (InvalidGameException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clearGames() {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE games";

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
