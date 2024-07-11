package chess;

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
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }


    private void loadEnemyAttackPositions (
            ChessPosition enemyPosition,
            ArrayList<ChessPosition> positions ) {
        ChessPiece enemy = gameBoard.getPiece(enemyPosition);
        Collection<ChessMove> attackMoves = enemy.pieceMoves(gameBoard, enemyPosition);
        for (ChessMove attackMove : attackMoves) {
            ChessPosition attackPosition = attackMove.getEndPosition();
            positions.add(attackPosition);
        }
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Map<ChessPiece, ArrayList<ChessPosition>> pieces = gameBoard.getPieces();
        ArrayList<ChessPosition> enemyAttackPositions = new ArrayList<ChessPosition>();
        
        ChessPosition kingPosition = null;
        for (ChessPiece piece : pieces.keySet()) {
            if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                kingPosition = pieces.get(piece).getFirst();
            } else if (piece.getTeamColor() != teamColor) {
                for (ChessPosition enemyPosition : pieces.get(piece)) {
                    loadEnemyAttackPositions(enemyPosition, enemyAttackPositions);
                }
            }
        }
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
