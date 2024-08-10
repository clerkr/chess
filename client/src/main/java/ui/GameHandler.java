package ui;

import model.GameData;

public interface GameHandler {
    void updateGame(GameData game);
    void printMessage(String message);
    void drawBoard();
}
