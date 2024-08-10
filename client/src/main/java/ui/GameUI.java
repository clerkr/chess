package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import dataaccess.ResponseException;
import execution.ClientExecution;
import websocket.WebSocketFacade;

public class GameUI implements GameHandler {

    private WebSocketFacade wsFacade = new WebSocketFacade(this, ClientExecution.PORT);

    public ChessGame game = new ChessGame();



    @Override
    public void updateGame(ChessGame game) {
        this.game = game;
    }

    @Override
    public void printMessage(String message) {
        System.out.print("\r\033[K");
        DrawPrompt.printNotification(message);
        DrawPrompt.drawGamePlayPrompt();
    }


    // rawPos examples: a1, e7
    private ChessPosition createChessPosition(String rawPos) throws Exception {
        String rowStr = rawPos.substring(1,2);
        int row = Integer.parseInt(rowStr);
        String colLetter = rawPos.substring(0,1);
        int col = DrawChessBoard.colNumsFromLetters.get(colLetter) + 1;

        if (!(
                DrawChessBoard.colNumsFromLetters.containsKey(colLetter) ||
                row < 1 || row > 8 ||
                col < 1 || col > 8
        )) {
            throw new Exception("Invalid coordinate format"); // TODO: specify this exception
        }

        return new ChessPosition(row, col);
    }

    private ChessMove createChessMove(ChessPosition start, ChessPosition end, ChessGame game) {
        ChessPiece piece = game.getBoard().getPiece(start);
        return new ChessMove(start, end, piece.getPieceType());
    }
}
