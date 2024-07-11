package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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



    /**
     * Gets a collection of valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard tempBoard;
        TeamColor tempTeamColor = teamTurn;
        ChessPiece piece = gameBoard.getPiece(startPosition);

        Collection<ChessMove> moves = piece.pieceMoves(gameBoard, startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();

       for (ChessMove move : moves) {
           setTeamTurn(piece.getTeamColor());
           tempBoard = new ChessBoard(gameBoard);
           try {
               makeMove(move);
               validMoves.add(move);
           } catch (InvalidMoveException ex) {

           }
           gameBoard = new ChessBoard(tempBoard);
       }
       setTeamTurn(tempTeamColor);
       return validMoves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard tempBoard = new ChessBoard(gameBoard);
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promo = move.getPromotionPiece();

        ChessPiece piece = gameBoard.getPiece(start);
        if (gameBoard.getPiece(start) == null) { throw new InvalidMoveException("Attempting to move a null piece"); }
        ArrayList<ChessMove> piecePossibleMoves = (ArrayList<ChessMove>) piece.pieceMoves(gameBoard, start);

        if (!piecePossibleMoves.contains(move)) {
            throw new InvalidMoveException("This is not a possible move for this piece");
        } else if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Out of turn move attempt");
        }

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && promo != null) {
            gameBoard.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            gameBoard.removePiece(move.getStartPosition());

            if (gameBoard.getPiece(start) != null) {
                throw new InvalidMoveException("After move, a piece is still present in the start position");
            }
            ChessPiece endPiece = gameBoard.getPiece(end);
            if (endPiece == null) {
                throw new InvalidMoveException("After move, no piece found at the end position");
            } else if (endPiece.getPieceType() != promo) {
                throw new InvalidMoveException("Found piece at end position is not the correct piece type");
            } else if (endPiece.getTeamColor() != piece.getTeamColor()) {
                throw new InvalidMoveException("Found piece at end position is the wrong team color");
            }
        } else {
            gameBoard.addPiece(move.getEndPosition(), piece);
            gameBoard.removePiece(move.getStartPosition());
        }

        if (isInCheck(piece.getTeamColor())) {
//            gameBoard = new ChessBoard(tempBoard); // maybe move this one out?
            throw new InvalidMoveException("The move not permissible as check results");
        }

        if ((teamTurn == TeamColor.WHITE)) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
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

    private boolean isValidatedMove (TeamColor teamColor) {
        Map<ChessPiece, ArrayList<ChessPosition>> pieces = (HashMap<ChessPiece, ArrayList<ChessPosition>>) gameBoard.getPieces();
        for (ChessPiece piece : pieces.keySet()) {
            for (ChessPosition piecePosition : pieces.get(piece)) {
                if (piece.getTeamColor() == teamColor) {
                    ArrayList<ChessMove> validatedMoves = (ArrayList<ChessMove>) validMoves(piecePosition);
                    if (!validatedMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) { return false; }
        return (isValidatedMove(teamColor));
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) { return false; }
        return (isValidatedMove(teamColor));
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
