package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;

public class DrawChessBoard {

    public static HashMap<Integer, String> colLettersfromNums = new HashMap<>(
            Map.of(
            0, "h",
            1, "g",
            2, "f",
            3, "e",
            4, "d",
            5, "c",
            6, "b",
            7, "a"
            )
    );
    public static Map<String, Integer> colNumsFromLetters = new HashMap<>(
            Map.of(
            "h", 0,
            "g", 1,
            "f", 2,
            "e", 3,
            "d", 4,
            "c", 5,
            "b", 6,
            "a", 7
            )
    );

    private static String drawPiece(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case PAWN -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
            case ROOK -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            case KNIGHT -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            case BISHOP -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            case QUEEN -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            case KING -> (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
        };
    }

    private static void drawBorderHeadFoot(String borderStyle,
                                           boolean whitePlayer,
                                           String resStyle) {
        System.out.print(borderStyle + "   ");
        for (int hcol = 0; hcol < 8; hcol++) {
            int hloc = whitePlayer ? 7 - hcol : hcol;
            System.out.print(" " + colLettersfromNums.get(hloc) + " ");
        }
        System.out.print("   " + resStyle + "\n");
    }

    public static void drawHighlightedBoard(ChessGame game, boolean whitePlayer, ChessPosition startPosition) {
        System.out.println();
        ChessPiece focusPiece = game.getBoard().getPiece(startPosition);
        if (focusPiece == null) {System.out.println("Empty position selected. Please provide a piece."); return;}

        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) game.validMoves(startPosition);
        HashSet<ChessPosition> validMovePositions = new HashSet<>();
        for (ChessMove m : moves) {
            validMovePositions.add(m.getEndPosition());
        }

        ChessPiece[][] board = game.getBoard().squares;
        String resStyle = EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR;
        String borderStyle = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
        // Header border
        drawBorderHeadFoot(borderStyle, whitePlayer, resStyle);
        // Main body
        for (int row = 0; row < 8; row++) {
            int wor = (whitePlayer) ? 7 - row : row;
            // Left border
            System.out.print(borderStyle + " " + (wor+1) + " " + resStyle);
            for (int col = 0; col < 8; col++) {
                int loc = (whitePlayer) ? 7 - col : col;
                ChessPosition boardPos = new ChessPosition(wor+1, loc+1);
                boolean validMoveCheck = validMovePositions.contains(boardPos);
                boolean lightBG = (wor+loc) % 2 == 0;
                String bg = (lightBG) ?
                        (validMoveCheck ? EscapeSequences.SET_BG_COLOR_GREEN: EscapeSequences.SET_BG_COLOR_LIGHT_GREY) :
                        (validMoveCheck ? EscapeSequences.SET_BG_COLOR_DARK_GREEN: EscapeSequences.SET_BG_COLOR_DARK_GREY);
                ChessPiece pos = board[wor][loc];
                String piece = (pos == null) ? ("   ") : drawPiece(pos);
                System.out.print(bg + piece + resStyle);
            }
            // Right border
            System.out.print(borderStyle + " " + (wor+1) + " " + resStyle);
            System.out.print("\n");
        }
        // Footer border
        drawBorderHeadFoot(borderStyle, whitePlayer, resStyle);
        System.out.print("\n");
    }

    public static void drawBoard(ChessGame game, boolean whitePlayer) {

        String turn = (game.getTeamTurn() == ChessGame.TeamColor.WHITE ?
                "WHITE" : "BLACK") + "'s turn";

        String status = !game.getIsOver() ?
                EscapeSequences.SET_TEXT_COLOR_GREEN + "Active" + EscapeSequences.RESET_TEXT_COLOR +
                    "\n" + turn :
                EscapeSequences.SET_TEXT_COLOR_RED + "Finished" + EscapeSequences.RESET_TEXT_COLOR;


        System.out.println("Status: " + status);

        ChessPiece[][] board = game.getBoard().squares;
        String resStyle = EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR;
        boolean lightBG = true;
        String borderStyle = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
        // Header border
        drawBorderHeadFoot(borderStyle, whitePlayer, resStyle);
        // Main body
        for (int row = 0; row < 8; row++) {
            int wor = (whitePlayer) ? 7 - row : row;
            // Left border
            System.out.print(borderStyle + " " + (wor+1) + " " + resStyle);
            for (int col = 0; col < 8; col++) {
                String bg = (lightBG) ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREY;
                lightBG = !lightBG;
                int loc = (whitePlayer) ? 7 - col : col;
                ChessPiece pos = board[wor][loc];
                String piece = (pos == null) ? ("   ") : drawPiece(pos);
                System.out.print(bg + piece + resStyle);
            }
            // Right border
            System.out.print(borderStyle + " " + (wor+1) + " " + resStyle);
            lightBG = !lightBG;
            System.out.print("\n");
        }
        // Footer border
        drawBorderHeadFoot(borderStyle, whitePlayer, resStyle);
        System.out.print("\n");
    }



}
