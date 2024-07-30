package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {

    private static MemoryGameDAO instance;
    private int gameID = 1;
    private HashSet<GameData> games = new HashSet<>();

    private MemoryGameDAO() {}

    public static synchronized MemoryGameDAO getInstance() {
        return (instance == null) ? (instance = new MemoryGameDAO()) : instance;
    }

    private void incrementGameID() {
        gameID += 1;
    }

    @Override
    public HashSet<GameData> listGames() {
        return games;
    }

    @Override
    public GameData getGame(int gameID) {
        for (GameData game : games) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public int createGame(String gameName) {
        int thisGameID = gameID;
        incrementGameID();
        GameData newGame = new GameData(thisGameID, null, null, gameName, new ChessGame());
        games.add(newGame);
        return thisGameID;
    }

    @Override
    public void updateGame(GameData updatedGame) throws InvalidGameException{
        boolean found = false;
        for (GameData game : games) {
            if (game.getGameID() == updatedGame.getGameID()) {
                found = true;
                if (game.getWhiteUsername() != updatedGame.getWhiteUsername()) {
                    game.setWhiteUsername(updatedGame.getWhiteUsername());
                }
                if (game.getBlackUsername() != updatedGame.getBlackUsername()) {
                    game.setBlackUsername(updatedGame.getBlackUsername());
                }
            }
        }
        if (!found) { throw new InvalidGameException("Game not found"); }
    }

    @Override
    public void clearGames() {
        games.clear();
        gameID = 1;
    }
}
