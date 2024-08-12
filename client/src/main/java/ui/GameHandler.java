package ui;

import chess.ChessPosition;
import model.GameData;

public interface GameHandler {
    void updateGame(GameData game);
    void printMessage(String message);
    void drawBoard();
    void drawHighlight(String startPos);
}
