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

    private ArrayList<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessMove move;
        int row;
        int col;

        int[][] directions = {
                {1,0},      // Up
                {0,-1},     // Left
                {-1,0},    // Down
                {0,1},     // Right
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

    private ArrayList<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessMove move;
        int row;
        int col;

        int[][] directions = {
                {1,1},      // Up-Right
                {1,-1},     // Up-Left
                {-1,-1},    // Down-Left
                {-1,1},     // Down-Right
                {1,0},      // Up
                {0,-1},     // Left
                {-1,0},    // Down
                {0,1},     // Right
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

    private ArrayList<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessMove move;
        int row;
        int col;

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

    private void promotions(ArrayList<ChessMove> moves,
                            ChessPosition myPosition,
                            ChessPosition endPosition) {
        moves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
        moves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
        moves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
    }
    private void pawnMove(ArrayList<ChessMove> moves,
                                ChessBoard board,
                                ChessPosition myPosition,
                                int row,
                                int col,
                                boolean attacking){

        if (!checkInBounds(row, col)){ return ;}

        if (attacking) {
            if (isPiece(board, row, col) &&
                board.getPiece(new ChessPosition(row, col)).getTeamColor() != pieceColor
            ) {
                ChessPosition endPosition = new ChessPosition(row, col);
                if (row == 1 || row == 8) {
                    promotions(moves, myPosition, endPosition);
                } else {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        } else {
            if (!isPiece(board, row, col)) {
                ChessPosition endPosition = new ChessPosition(row, col);
                if (row == 1 || row == 8) {
                    promotions(moves, myPosition, endPosition);
                } else {
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }

        }
    }

    private ArrayList<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessMove move;
        int row;
        int col;
        boolean on_start_check = (pieceColor == ChessGame.TeamColor.WHITE) ?
                (myPosition.getRow() == 2) : (myPosition.getRow() == 7);
        int mover = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;

        int[][] attack_directions = { {1,-1}, {1,1}};
        for (int[] direction : attack_directions) {
            int row_dir = direction[0] * mover;
            int col_dir = direction[1];
            row = myPosition.getRow() + row_dir;
            col = myPosition.getColumn() + col_dir;
            pawnMove(moves, board, myPosition, row, col, true);
        }
        // Forward
        row = myPosition.getRow() + mover;
        col = myPosition.getColumn();
        pawnMove(moves, board, myPosition, row, col, false);

        // Forward 2x
        if (on_start_check) {
            row = myPosition.getRow() + mover * 2;
            if (!isPiece(board, row - mover, col) && !isPiece(board, row, col)) {
                pawnMove(moves, board, myPosition, row, col, false);
            }
        }
        return moves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        return switch (type) {
            case BISHOP -> bishopMoves(board, myPosition);
            case KING -> kingMoves(board, myPosition);
            case KNIGHT -> knightMoves(board, myPosition);
            case PAWN -> pawnMoves(board, myPosition);
            case ROOK -> rookMoves(board, myPosition);
            case QUEEN -> queenMoves(board, myPosition);
            default -> throw new IllegalArgumentException("Invalid piece type: " + type);
        };

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
