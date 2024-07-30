package dataAccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    HashSet<GameData> listGames() throws Exception;
    GameData getGame(int gameID) throws Exception;
    int createGame(String gameName) throws Exception;
    void updateGame(GameData updatedGame) throws Exception;
    void clearGames() throws Exception;
}
