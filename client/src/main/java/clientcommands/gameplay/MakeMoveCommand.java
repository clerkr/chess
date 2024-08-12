package clientcommands.gameplay;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.InvalidMoveException;
import clientcommands.Command;
import clientcommands.gameplay.exceptions.*;
import execution.ClientExecution;
import facade.ServerFacade;
import ui.DrawChessBoard;

import java.util.Map;

public class MakeMoveCommand implements Command {

    ClientExecution client = ClientExecution.getInstance();
    ServerFacade facade = ClientExecution.FACADE;

    Map<String, Integer> colNumsFromLetters = DrawChessBoard.colNumsFromLetters;

    @Override
    public void execute() {
        if (observingCheck()) { return; }

        if (client.parsed.length < 3 || client.parsed.length > 4) {
            System.out.println("Incorrect argument arrangement\nUse 'help' for command guides");
            return;
        }
        try {
            boolean gameOver = facade.gameOverCheck();
            if (gameOver) {
                throw new GameOverException("ERROR: No moves can be made. Game is over");
            }

            boolean playersTurn = facade.checkPlayersTurn();
            if (!playersTurn) {
                throw new OutOfTurnAttemptException("ERROR: It is not your turn");
            }

            ChessPiece.PieceType promotionType = null;
            if (client.parsed.length == 4) {
                String rawPromType = client.parsed[3].toLowerCase();
                promotionType = findPromotionPiece(rawPromType);
            }

            String startCoord = client.parsed[1];
            String endCoord = client.parsed[2];

            ChessMove move = prepMove(startCoord, endCoord, promotionType);

            if (!facade.checkPiecePlayersColor(move)) {
                throw new OpponentPieceMovementException("ERROR: Attempted to move opponent piece");
            }

            if (!facade.checkValidMove(move)) {
                throw new InvalidMoveException("ERROR: Invalid move for selected piece");
            }
            facade.sendMakeMoveHandler(client.authToken, client.gamePlayGameID, move);


        } catch (NullPointerException e) {
            System.out.println("ERROR: No piece selected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    private boolean observingCheck() {
        if (client.observing) {
            System.out.println("Observers cannot move pieces");
            return true;
        }
        return false;
    }

    private ChessPiece.PieceType findPromotionPiece(String rawPromType) throws PromotionTypeException {
        ChessPiece.PieceType promotionType;
        if (rawPromType.contains("rook")) {
            promotionType = ChessPiece.PieceType.ROOK;
        } else if (rawPromType.contains("knight")) {
            promotionType = ChessPiece.PieceType.KNIGHT;
        } else if (rawPromType.contains("bishop")) {
            promotionType = ChessPiece.PieceType.BISHOP;
        } else if (rawPromType.contains("queen")) {
            promotionType = ChessPiece.PieceType.QUEEN;
        } else {
            throw new PromotionTypeException("ERROR: Invalid pawn promotion piece type");
        }


        return promotionType;
    }

    private ChessMove prepMove(String startPos,
                               String endPos,
                               ChessPiece.PieceType promotionType)
            throws ColumnLetterFormatException,
            CoordinateFormatException,
            NumberFormatException
    {
        ChessPosition start = parseCoordStringAsPos(startPos);
        ChessPosition end = parseCoordStringAsPos(endPos);
        return new ChessMove(start, end, promotionType);

    }

    private ChessPosition parseCoordStringAsPos(String coord)
            throws CoordinateFormatException, NumberFormatException, ColumnLetterFormatException {

        if (coord.length() != 2) {throw new CoordinateFormatException("ERROR: Invalid coordinate format");}

        String startColLetter = coord.substring(0,1);
        if (!colNumsFromLetters.containsKey(startColLetter)) {
            System.out.println();
            throw new ColumnLetterFormatException("ERROR: positions can only have a column letter of a-h");
        }
        int col = colNumsFromLetters.get(startColLetter) + 1;

        int row = Integer.parseInt(coord.substring(1, 2));
        if (row < 1 || row > 8) {throw new NumberFormatException("ERROR: Row num out of range");}

        return new ChessPosition(row, col);
    }





}
