package dataaccess.interfaces;

import dataaccess.InvalidGameException;
import model.GameData;

import java.util.HashSet;

public interface GameDAO {
    HashSet<GameData> listGames();
    GameData getGame(int gameID);
    int createGame(String gameName);
    void updateGame(GameData updatedGame) throws InvalidGameException;
    void clearGames() throws Exception;
}
