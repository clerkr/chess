package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {

    private static MemoryGameDAO instance;
    private int gameID = 1;
    private ArrayList<GameData> games = new ArrayList<>();

    private MemoryGameDAO() {}

    public static synchronized MemoryGameDAO getInstance() {
        return (instance == null) ? (instance = new MemoryGameDAO()) : instance;
    }

    private void incrementGameID() {
        gameID += 1;
    }

    public ArrayList<GameData> listGames() {
        return games;
    }

    public GameData getGame(int gameID) {
        for (GameData game : games) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    public int createGame(String gameName) {
        int thisGameID = gameID;
        incrementGameID();
        GameData newGame = new GameData(thisGameID, null, null, gameName, new ChessGame());
        games.add(newGame);
        return thisGameID;
    }

    public void updateGame(GameData updatedGame) throws InvalidGameException{
//        String whiteuser = updatedGame.getWhiteUsername();
//        String blackuser = updatedGame.getBlackUsername();
        boolean joined = false;
        for (GameData game : games) {
            if (game.getGameID() == updatedGame.getGameID()) {
//                game.setWhiteUsername(whiteuser);
//                game.setBlackUsername(blackuser);
                game = updatedGame;
                joined = true;
            }
        }
        if (!joined) { throw new InvalidGameException("Game not found"); }
    }

    @Override
    public void clearGames() {
        games.clear();
    }
}
