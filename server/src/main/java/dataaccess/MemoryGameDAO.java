package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {

    private static MemoryGameDAO instance;
    private ArrayList<GameData> games = new ArrayList<>();

    private MemoryGameDAO() {}

    public static synchronized MemoryGameDAO getInstance() {
        return (instance == null) ? (instance = new MemoryGameDAO()) : instance;
    }

    @Override
    public void clearGames() {
        games.clear();
    }
}
