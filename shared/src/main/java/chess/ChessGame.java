package chess;

import java.util.*;

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

    public ChessGame(ChessGame that) {
        this.gameBoard = that.gameBoard;
        this.teamTurn = that.teamTurn;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, teamTurn);
    }

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
        // Preserves teamTurn state so that the makeMove() manipulation of teamTurn is imperminant
        TeamColor tempTeamColor = teamTurn;
        ChessPiece piece = gameBoard.getPiece(startPosition);

        Collection<ChessMove> moves = piece.pieceMoves(gameBoard, startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<>();

       for (ChessMove move : moves) {
           setTeamTurn(piece.getTeamColor());
           tempBoard = new ChessBoard(gameBoard);
           try {
               makeMove(move);
               validMoves.add(move);
           } catch (InvalidMoveException ex) {
                System.out.println(ex);
           }
           gameBoard = new ChessBoard(tempBoard);
       }

       // Reset teamTurn
       setTeamTurn(tempTeamColor);
       return validMoves;
    }

    private void invertTeamTurn() {
        if ((teamTurn == TeamColor.WHITE)) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
    }
    private void movePiece(ChessMove move, ChessPiece piece) {
        gameBoard.addPiece(move.getEndPosition(), piece);
        gameBoard.removePiece(move.getStartPosition());
    }
    private void validatePawnPromoEndState(ChessPiece startPiece,
                                           ChessPiece endPiece,
                                           ChessPiece.PieceType promo) throws InvalidMoveException{
        if (endPiece == null ||
                endPiece.getPieceType() != promo ||
                endPiece.getTeamColor() != startPiece.getTeamColor()) {
            throw new InvalidMoveException("End state pawn promotion issue: Expected a %s %s but got %s %s",
                    startPiece.getTeamColor(), promo,
                    endPiece == null ? "none" : endPiece.getTeamColor(), endPiece == null ? "none" : endPiece.getPieceType());
        }
    }
    private void handlePawnPromotion(ChessPiece piece,
                                     ChessPosition start,
                                     ChessPosition end,
                                     ChessPiece.PieceType promo) throws InvalidMoveException {
        gameBoard.addPiece(end, new ChessPiece(piece.getTeamColor(), promo));
        gameBoard.removePiece(start);
        if (gameBoard.getPiece(start) != null) {
            throw new InvalidMoveException("After move, a piece is still present in the start position");
        }
        validatePawnPromoEndState(piece, gameBoard.getPiece(end), promo);
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece piece = gameBoard.getPiece(move.getStartPosition());
        if (piece == null) { throw new InvalidMoveException("Attempting to move a null piece"); }

        Collection<ChessMove> piecePossibleMoves = piece.pieceMoves(gameBoard, move.getStartPosition());
        if (!piecePossibleMoves.contains(move)) {
            throw new InvalidMoveException("This is not a possible move for this piece");
        }

        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Out of turn move attempt");
        }

        ChessBoard tempBoard = new ChessBoard(gameBoard);
        try {
            if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
                handlePawnPromotion(piece, move.getStartPosition(), move.getEndPosition(), move.getPromotionPiece());
            } else {
                movePiece(move, piece);
            }
            if (isInCheck(piece.getTeamColor())) {
                throw new InvalidMoveException("The move not permissible as it results in check");
            }
        } finally {
            if (isInCheck(piece.getTeamColor())) {
                gameBoard = new ChessBoard(tempBoard); // Reverts move to previous board condition if it results in Check
            }
        }
        invertTeamTurn();
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
