package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
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

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    private void resetBackHelper(ChessGame.TeamColor teamColor, int row, ChessPiece.PieceType[] side_pieces){

        for (int i = 0; i < 3; i++) {
            squares[row][i] = new ChessPiece(teamColor, side_pieces[i]);
        }
        squares[row][3] = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
        squares[row][4] = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        for (int i = 2; i >= 0; i--) {
            squares[row][7-i] = new ChessPiece(teamColor, side_pieces[i]);
        }
    }

    public void resetBoard() {
        ChessPiece.PieceType[] side_pieces = new ChessPiece.PieceType[3];
        side_pieces[0] = ChessPiece.PieceType.ROOK;
        side_pieces[1] = ChessPiece.PieceType.KNIGHT;
        side_pieces[2] = ChessPiece.PieceType.BISHOP;

        for (int row = 0; row < 8; row++) {

            if (row == 0) {
                resetBackHelper(ChessGame.TeamColor.WHITE, row, side_pieces);
            } else if (row == 1) {
                for (int i = 0; i < 8; i++) {
                    squares[row][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
                }
            } else if (row > 1 && row < 6) {
                for (ChessPiece square : squares[row]) {
                    square = null;
                }
            } else if (row == 6) {
                for (int i = 0; i < 8; i++) {
                    squares[row][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
                }
            } else if (row == 7) {
                resetBackHelper(ChessGame.TeamColor.BLACK, row, side_pieces);
            }




        }
    }


}
