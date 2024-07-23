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

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public ChessPiece(ChessPiece other) {
        this.pieceColor = other.pieceColor;
        this.type = other.type;
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

    private boolean inBoundsCheck(int row, int col) {
        return (row >= 1 && row <= 8 && col >= 1 && col <= 8);
    }
    private boolean isPiece (ChessBoard board, int row, int col) {
        if (!inBoundsCheck(row, col)) { return false;}
        return board.getPiece(new ChessPosition(row, col)) != null;
    }
    private boolean isFriend (ChessBoard board, int row, int col) {
        if (!inBoundsCheck(row, col)) { return false;}
        return board.getPiece(new ChessPosition(row, col)).pieceColor == pieceColor;
    }
    private void slidingMove (ArrayList<ChessMove> moves,
                      ChessBoard board,
                      ChessPosition myPosition,
                      int[][] directions) {
        int startingRow = myPosition.getRow();
        int startingCol = myPosition.getColumn();
        int row;
        int col;
        int multiplier;

        for (int[] direction : directions) {
            int rowDir = direction[0];
            int colDir = direction[1];
            row = startingRow + rowDir;
            col = startingCol + colDir;
            multiplier = 1;
            while(true){
                if (!inBoundsCheck(row, col)) { break; }
                if (isPiece(board, row, col) && isFriend(board, row, col)) { break; }

                ChessPosition endPosition = new ChessPosition(row, col);
                ChessMove move = new ChessMove(myPosition, endPosition, null);
                moves.add(move);

                if (isPiece(board, row, col) && !isFriend(board, row, col)) { break; }
                multiplier += 1;
                row = startingRow + rowDir * multiplier;
                col = startingCol + colDir * multiplier;
            }
        }
    }
    private void singleMove (ArrayList<ChessMove> moves,
                      ChessBoard board,
                      ChessPosition myPosition,
                      int[][] directions) {
        int startingRow = myPosition.getRow();
        int startingCol = myPosition.getColumn();
        int row;
        int col;

        for (int[] direction : directions) {
            int rowDir = direction[0];
            int colDir = direction[1];
            row = startingRow + rowDir;
            col = startingCol + colDir;

            if (!inBoundsCheck(row, col)) { continue; }
            if (isPiece(board, row, col) && isFriend(board, row, col)) { continue; }

            ChessPosition endPosition = new ChessPosition(row, col);
            ChessMove move = new ChessMove(myPosition, endPosition, null);
            moves.add(move);
        }
    }

    private void pawnPromoter (ArrayList<ChessMove> moves,
                       ChessPosition myPosition,
                       ChessPosition endPosition) {
        moves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
        moves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
        moves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
    }
    private ArrayList<ChessMove> pawnMoves (ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int direction;
        int initialRow;
        int promotionRow;
        int startingRow = myPosition.getRow();
        int startingCol = myPosition.getColumn();
        int row;
        int col;

        if (pieceColor == ChessGame.TeamColor.WHITE) {
            direction = 1;
            initialRow = 2;
            promotionRow = 8;
        } else {
            direction = -1;
            initialRow = 7;
            promotionRow = 1;
        }

        int[][] attackDirections = { {1,1}, {1,-1} };

        // Attacking
        for (int[] atckDir : attackDirections) {
            int rowDir = atckDir[0] * direction;
            int colDir = atckDir[1];
            row = startingRow + rowDir;
            col = startingCol + colDir;

            if (!inBoundsCheck(row, col)) { continue; }

            if (isPiece(board, row, col) && !isFriend(board, row, col)) {
                ChessPosition endPosition = new ChessPosition(row, col);
                if (row == promotionRow) {
                    pawnPromoter(moves, myPosition, endPosition);
                } else {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }

        // Forward
        row = startingRow + direction;
        col = startingCol;
        if (inBoundsCheck(row, col) && !isPiece(board, row, col)) {
            ChessPosition endPosition = new ChessPosition(row, col);
            if (row == promotionRow) {
                pawnPromoter(moves, myPosition, endPosition);
            } else {
                moves.add(new ChessMove(myPosition, endPosition, null));
            }

            if (startingRow == initialRow &&
                !isPiece(board, row + direction, col))
            {
                endPosition = new ChessPosition(row + direction, col);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        return moves;
    }

    private ArrayList<ChessMove> rookMoves (ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = {

                {1,0},

                {0,-1},

                {-1,0},

                {0,1}
        };

        slidingMove(moves, board, myPosition, directions);
        return moves;
    }

    private ArrayList<ChessMove> knightMoves (ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = {
                {2,1},
                {2,-1},
                {1,-2},
                {-1,-2},
                {-2,-1},
                {-2,1},
                {-1,2},
                {1,2}
        };

        singleMove(moves, board, myPosition, directions);
        return moves;
    }

    private ArrayList<ChessMove> bishopMoves (ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = {
                {1,1},
                {1,-1},
                {-1,-1},
                {-1,1}
        };

        slidingMove(moves, board, myPosition, directions);
        return moves;
    }

    private ArrayList<ChessMove> queenMoves (ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = {
                {1,1},
                {1,0},
                {1,-1},
                {0,-1},
                {-1,-1},
                {-1,0},
                {-1,1},
                {0,1}
        };

        slidingMove(moves, board, myPosition, directions);
        return moves;
    }

    private ArrayList<ChessMove> kingMoves (ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = {
                {1,1},
                {1,0},
                {1,-1},
                {0,-1},
                {-1,-1},
                {-1,0},
                {-1,1},
                {0,1}
        };

        singleMove(moves, board, myPosition, directions);
        return moves;
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        return switch (type) {
            case PAWN -> pawnMoves(board, myPosition);
            case ROOK -> rookMoves(board, myPosition);
            case KNIGHT -> knightMoves(board, myPosition);
            case BISHOP -> bishopMoves(board, myPosition);
            case QUEEN -> queenMoves(board, myPosition);
            case KING -> kingMoves(board, myPosition);
        };

    }
}
