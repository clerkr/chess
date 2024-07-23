package chess;

import java.sql.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {}

    public ChessBoard(ChessBoard other) {
        squares = new ChessPiece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (other.squares[row][col] == null) {
                    squares[row][col] = null;
                } else {
                    squares[row][col] = new ChessPiece(other.squares[row][col]);
                }
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    public Map<ChessPiece, ArrayList<ChessPosition>> getPieces () {
        Map<ChessPiece, ArrayList<ChessPosition>> pieces = new HashMap<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = squares[row][col];
                ChessPosition position = new ChessPosition(row+1, col+1);
                if (piece == null){ continue; }
                if (!pieces.containsKey(piece)) {
                    ArrayList<ChessPosition> positionList = new ArrayList<ChessPosition>();
                    positionList.add(position);
                    pieces.put(piece, positionList);
                } else {
                    pieces.get(piece).add(position);
                }
            }
        }
        return pieces;
    }


    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    void backPieceSetter (int row, int col, ChessGame.TeamColor teamColor) {
        if (col == 0 || col == 7) {
            squares[row][col] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        } else if (col == 1 || col == 6) {
            squares[row][col] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        } else if (col == 2 || col == 5) {
            squares[row][col] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        } else if (col == 3) {
            squares[row][col] = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
        } else if (col == 4) {
            squares[row][col] = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        }
    }
    public void resetBoard() {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 8; row ++){
                
                if (row > 1 && row < 6) {
                    squares[row][col] = null;
                } else if (row == 1) {
                    squares[row][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
                } else if (row == 6) {
                    squares[row][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
                } else if (row == 0) {
                    backPieceSetter(row, col, ChessGame.TeamColor.WHITE);
                } else if (row == 7) {
                    backPieceSetter(row, col, ChessGame.TeamColor.BLACK);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
