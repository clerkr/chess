package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor,
                      ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    private boolean checkInBounds(int row, int col) {
        return (row >= 1 && row <= 8) && (col >= 1 && col <= 8);
    }
    private boolean isPiece(ChessBoard board, int row, int col){
        return board.getPiece(new ChessPosition(row, col)) != null;// Piece is present
    }
    private boolean isFriend(ChessBoard board, int row, int col){
        ChessPosition pos = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(pos);
        return piece.pieceColor == pieceColor;

    }

    private ArrayList<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessMove move;
        int row;
        int col;

        int[][] directions = {
                {1,1},      // Up-Right
                {1,-1},     // Up-Left
                {-1,-1},    // Down-Left
                {-1,1},     // Down-Right
        };
        for (int[] direction : directions) {
            int multiplier = 1;
            int row_dir = direction[0];
            int col_dir = direction[1];
            row = myPosition.getRow() + row_dir;
            col = myPosition.getColumn() + col_dir;
            while(true) {
                if (!checkInBounds(row, col)) { break; }
                if (isPiece(board, row, col) && isFriend(board, row, col)) { break; }

                ChessPosition endPosition = new ChessPosition(row, col);
                move = new ChessMove(myPosition, endPosition, null);
                moves.add(move);
                if (isPiece(board, row, col) && !isFriend(board, row, col)) { break; }

                multiplier += 1;
                row = myPosition.getRow() + row_dir * multiplier;
                col = myPosition.getColumn() + col_dir * multiplier;
            }
        }
        return moves;
    }

    private ArrayList<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessMove move;
        int row;
        int col;

        int[][] directions = {
                {1,1},  // Up-Right
                {1,0},  // Up
                {1,-1}, // Up-Left
                {0,-1}, // Left
                {-1,-1},// Down-Left
                {-1,0}, // Down
                {-1,1}, // Down-Right
                {0,1}  // Right
        };

        for (int[] direction : directions) {
            row = myPosition.getRow() + direction[0];
            col = myPosition.getColumn() + direction[1];
            if (checkInBounds(row, col)) {
                if (!isPiece(board, row, col) || (isPiece(board, row, col) && !isFriend(board, row, col))) {
                    ChessPosition endPosition = new ChessPosition(row, col);
                    move = new ChessMove(myPosition, endPosition, null);
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        if (type == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        } else if (type == PieceType.KING) {
            return kingMoves(board, myPosition);
        }
//        return bishopMoves(myPosition);
        return new ArrayList<ChessMove>();




    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
