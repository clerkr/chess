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
    public void resetBoard() {
        for (int row = 0; row < 8; row++) {


            ChessPiece.PieceType[] side_pieces = new ChessPiece.PieceType[3];
            side_pieces[0] = ChessPiece.PieceType.ROOK;
            side_pieces[1] = ChessPiece.PieceType.KNIGHT;
            side_pieces[2] = ChessPiece.PieceType.BISHOP;

            if (row == 0) {
                for (int i = 0; i < 3; i++) {
                    squares[row][i] = new ChessPiece(ChessGame.TeamColor.WHITE, side_pieces[i]);
                }
                squares[row][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
                squares[row][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
                for (int i = 2; i >= 0; i--) {
                    squares[row][7-i] = new ChessPiece(ChessGame.TeamColor.WHITE, side_pieces[i]);
                }
            }
            if (row == 7) {
                for (int i = 0; i < 3; i++) {
                    squares[row][i] = new ChessPiece(ChessGame.TeamColor.BLACK, side_pieces[i]);
                }
                squares[row][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
                squares[row][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
                for (int i = 2; i >= 0; i--) {
                    squares[row][7-i] = new ChessPiece(ChessGame.TeamColor.BLACK, side_pieces[i]);
                }
            }

            // Pawns
            if (row == 1) {
                for (int i = 0; i < 8; i++) {
                    squares[row][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
                }
            } else if (row == 6) {
                for (int i = 0; i < 8; i++) {
                    squares[row][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
                }
            }

            if (row > 1 && row < 6) {
                for (ChessPiece square : squares[row]) {
                    square = null;
                }
            }

        }
    }


}
