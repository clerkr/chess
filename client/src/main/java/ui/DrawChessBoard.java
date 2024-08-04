package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.util.HashMap;

public class DrawChessBoard {

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
        HashMap<Integer, String> colLetters = new HashMap<>();
        colLetters.put(0, "h");
        colLetters.put(1, "g");
        colLetters.put(2, "f");
        colLetters.put(3, "e");
        colLetters.put(4, "d");
        colLetters.put(5, "c");
        colLetters.put(6, "b");
        colLetters.put(7, "a");
        System.out.print(borderStyle + "   ");
        for (int hcol = 0; hcol < 8; hcol++) {
            int hloc = whitePlayer ? 7 - hcol : hcol;
            System.out.print(" " + colLetters.get(hloc) + " ");
        }
        System.out.print("   " + resStyle + "\n");
    }

    private static void drawBoard(ChessGame game, boolean whitePlayer) {
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
    public static void drawBoards(ChessGame game) {
        System.out.println();
        drawBoard(game, false);
        drawBoard(game, true);
    }


}
