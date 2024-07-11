package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard gameBoard = new ChessBoard();
    TeamColor teamTurn = TeamColor.WHITE;

    public ChessGame() {
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() { return teamTurn; }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) { teamTurn = team; }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private ChessBoard copyGameBoard() {
        return new ChessBoard(gameBoard);
    }

    private void mover (ChessBoard board, ChessPiece piece, ChessMove move) throws InvalidMoveException {

        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promo = move.getPromotionPiece();

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            board.removePiece(move.getStartPosition());

            if (board.getPiece(start) != null) {
                throw new InvalidMoveException("After move, a piece is still present in the start position");
            }
            ChessPiece endPiece = board.getPiece(end);
            if (endPiece == null) {
                throw new InvalidMoveException("After move, no piece found at the end position");
            } else if (endPiece.getPieceType() != promo) {
                throw new InvalidMoveException("Found piece at end position is not the correct piece type");
            } else if (endPiece.getTeamColor() != piece.getTeamColor()) {
                throw new InvalidMoveException("Found piece at end position is the wrong team color");
            }

        } else {
            board.addPiece(move.getEndPosition(), piece);
            board.removePiece(move.getStartPosition());

        }




    }
    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(gameBoard, startPosition);

        if (isInCheck(piece.getTeamColor())) {
            return moves;
        }
        return new ArrayList<ChessMove>();
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard tempBoard = copyGameBoard();
        ChessPosition start = move.getStartPosition();
        if (gameBoard.getPiece(start) == null) {
            throw new InvalidMoveException("Attempting to move a null piece");
        }
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promo = move.getPromotionPiece();

        ChessPiece piece = gameBoard.getPiece(start);
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Move attempt out of turn");
        }
        if (gameBoard.getPiece(start) == null) {
            throw new InvalidMoveException("No piece at move start position");
        }
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) piece.pieceMoves(gameBoard, start);
        if (!moves.contains(move)) {
            throw new InvalidMoveException("This is an invalid move");
        }
        mover(gameBoard, piece, move);
        if (isInCheck(piece.getTeamColor())) {
            setBoard(tempBoard);
            throw new InvalidMoveException("This move leads to check");
        }

        teamTurn = (teamTurn == TeamColor.WHITE) ? (TeamColor.BLACK) : (TeamColor.WHITE);
    }


    private ChessPosition getKingPosition (
            TeamColor team,
            Map<ChessPiece, ArrayList<ChessPosition>> pieces) {
        for (ChessPiece piece : pieces.keySet()) {
            if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == team) {
                return pieces.get(piece).getFirst();
            }
        }
        return new ChessPosition(1,1); // This should never return since there should always be a team king
    }
    private ArrayList<ChessPosition> getEnemyAttackPositions (
            TeamColor team,
            Map<ChessPiece, ArrayList<ChessPosition>> pieces) {

        ArrayList<ChessPosition> enemyAttackPositions = new ArrayList<ChessPosition>();
        for (ChessPiece piece : pieces.keySet()) {
            if (piece.getTeamColor() != team) {
                for (ChessPosition enemyPosition : pieces.get(piece)) {
                    ChessPiece enemy = gameBoard.getPiece(enemyPosition);
                    Collection<ChessMove> attackMoves = enemy.pieceMoves(gameBoard, enemyPosition);
                    for (ChessMove attackMove : attackMoves) {
                        ChessPosition attackPosition = attackMove.getEndPosition();
                        enemyAttackPositions.add(attackPosition);
                    }
                }
            }
        }

        return enemyAttackPositions;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Map<ChessPiece, ArrayList<ChessPosition>> pieces = gameBoard.getPieces();
        
        ChessPosition kingPosition = getKingPosition(teamColor, pieces);
        ArrayList<ChessPosition> enemyAttackPositions = getEnemyAttackPositions(teamColor, pieces);
        return enemyAttackPositions.contains(kingPosition);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) { gameBoard = new ChessBoard(board); }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() { return gameBoard; }
}
