package ui;

import chess.*;
import model.GameData;
import execution.ClientExecution;
import websocket.WebSocketFacade;

import java.util.Objects;

public class GameUI implements GameHandler {

    private WebSocketFacade wsFacade = new WebSocketFacade(this, ClientExecution.PORT);
    private ClientExecution client = ClientExecution.getInstance();
    public GameData gameData;

    @Override
    public void drawBoard() {
        boolean whitePlayerCheck = !Objects.equals(client.username, gameData.getBlackUsername());

        System.out.print("\r\033[K");
        DrawChessBoard.drawBoard(gameData.getGame(), whitePlayerCheck);
    }

    public void drawHighlight(String startPos) {
        try {
            boolean whitePlayerCheck = !Objects.equals(client.username, gameData.getBlackUsername());
            System.out.print("\r\033[K");
            DrawChessBoard.drawHighlightedBoard(gameData.getGame(), whitePlayerCheck, createChessPosition(startPos));
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData gameData) {
        this.gameData = gameData;
//        drawBoard();
//        DrawPrompt.drawGamePlayPrompt();
    }

    @Override
    public void printMessage(String message) {
        System.out.print("\r\033[K");
        DrawPrompt.printNotification(message);
        DrawPrompt.drawGamePlayPrompt();
    }




    // rawPos examples: a1, e7
    private ChessPosition createChessPosition(String rawPos) throws Exception {
            String rowStr = rawPos.substring(1, 2);
            int row = Integer.parseInt(rowStr);
            String colLetter = rawPos.substring(0, 1);
            int col = DrawChessBoard.colNumsFromLetters.get(colLetter) + 1;

            if (!(
                    DrawChessBoard.colNumsFromLetters.containsKey(colLetter) ||
                            row < 1 || row > 8 ||
                            col < 1 || col > 8
            )) {
                throw new Exception("Invalid coordinate format");
            }

            return new ChessPosition(row, col);

    }

    public ChessGame.TeamColor getPlayerTeam() {
        boolean whiteUserCheck = Objects.equals(gameData.getWhiteUsername(), client.username);
        return whiteUserCheck ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
    }


    public boolean checkPlayersTurn() {

        ChessGame.TeamColor teamTurn = gameData.getGame().getTeamTurn();
        ChessGame.TeamColor playerColor = getPlayerTeam();

        return teamTurn == playerColor;
    }

    public boolean checkPiecePlayersColor(ChessMove move) {
        ChessGame.TeamColor pieceColor =
            gameData.getGame().getBoard().getPiece(move.getStartPosition()).getTeamColor();
        String whiteGameUsername = gameData.getWhiteUsername();
        String blackGameUsername =  gameData.getBlackUsername();
        if (Objects.equals(whiteGameUsername, client.username) && ChessGame.TeamColor.WHITE == pieceColor) {
            return true;
        } else {
            return Objects.equals(blackGameUsername, client.username) && ChessGame.TeamColor.BLACK == pieceColor;
        }
    }

    public boolean checkValidMove(ChessMove move) {
        ChessGame tempGame = new ChessGame(gameData.getGame());
        try {
            tempGame.makeMove(move);
        } catch (InvalidMoveException e) {
            return false;
        }
        return true;

    }


}
