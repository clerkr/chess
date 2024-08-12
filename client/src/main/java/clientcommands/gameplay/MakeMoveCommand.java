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
                throw new OutOfTurnAttemptException("");
            }

            String rawPromType = client.parsed[3].toLowerCase();
            ChessPiece.PieceType promotionType = findPromotionPiece(rawPromType);

            String startCoord = client.parsed[1];
            String endCoord = client.parsed[2];

            ChessPosition startPosition = parseCoordStringAsPos(startCoord);
            ChessPosition endPosition = parseCoordStringAsPos(endCoord);

            ChessMove move = prepMove(startCoord, endCoord, promotionType);

            if (!facade.checkPiecePlayersColor(move)) {
                throw new OpponentPieceMovementException("");
            }

            if (!facade.checkValidMove(move)) {
                throw new InvalidMoveException("");
            }
            facade.sendMakeMoveHandler(client.authToken, client.gamePlayGameID, move);


        } catch (ColumnLetterFormatException e) {
            System.out.println("ERROR: positions can only have a column letter of a-h");
        } catch (NumberFormatException e) {
            System.out.println("ERROR: row numbers must be between 1-8");
        } catch (CoordinateFormatException e) {
            System.out.println("ERROR: Invalid coordinate format");
        } catch (PromotionTypeException e) {
            System.out.println("ERROR: Invalid pawn promotion piece type");
        } catch (OpponentPieceMovementException e) {
            System.out.println("ERROR: Attempted to move opponent piece");
        } catch (InvalidMoveException e) {
            System.out.println("ERROR: Invalid move for selected piece");
        } catch (OutOfTurnAttemptException e) {
            System.out.println("ERROR: It is not your turn");
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
        ChessPiece.PieceType promotionType = null;

        if (client.parsed.length == 4) {
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
        }

        return promotionType;
    }

    private ChessMove prepMove(String startPos,
                               String endPos,
                               ChessPiece.PieceType promotionType) throws ColumnLetterFormatException, CoordinateFormatException {
        ChessPosition start = parseCoordStringAsPos(startPos);
        ChessPosition end = parseCoordStringAsPos(endPos);
        return new ChessMove(start, end, promotionType);
    }

    private ChessPosition parseCoordStringAsPos(String coord) throws CoordinateFormatException, NumberFormatException, ColumnLetterFormatException {

        if (coord.length() != 2) {throw new CoordinateFormatException("");}

        String startColLetter = coord.substring(0,1);
        if (!colNumsFromLetters.containsKey(startColLetter)) {
            System.out.println();
            throw new ColumnLetterFormatException("placeholder message");
        }
        int col = colNumsFromLetters.get(startColLetter) + 1;

        int row = Integer.parseInt(coord.substring(1, 2));
        if (row < 1 || row > 8) {throw new NumberFormatException("Row num out of range");}

        return new ChessPosition(row, col);
    }





}
