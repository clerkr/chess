import Facade.FacadeGameData;
import Facade.ServerFacade;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;
import model.UserData;
import org.glassfish.grizzly.utils.Pair;
import ui.EscapeSequences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static final ServerFacade facade = new ServerFacade(8080);
    private static final Scanner scanner = new Scanner(System.in);

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

    private static void drawBoards(ChessGame game, boolean whitePlayer) {
        ChessPiece[][] board = game.getBoard().squares;
        String resBg = EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR;
        boolean lightBG = true;
        // Header and footer borders
        HashMap<Integer, String> colLetters = new HashMap<>();
            colLetters.put(0, "h");
            colLetters.put(1, "g");
            colLetters.put(2, "f");
            colLetters.put(3, "e");
            colLetters.put(4, "d");
            colLetters.put(5, "c");
            colLetters.put(6, "b");
            colLetters.put(7, "a");
        String borderStyle = EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
        System.out.print(borderStyle + "   ");
        for (int hcol = 0; hcol < 8; hcol++) {
            int hloc = whitePlayer ? 7 - hcol : hcol;
            System.out.print(" " + colLetters.get(hloc) + " ");
        }
        System.out.print("   " + resBg + "\n");
        // Main body
        for (int row = 0; row < 8; row++) {
            int wor = (whitePlayer) ? 7 - row : row;
            System.out.print(borderStyle + " " + (wor+1) + " " + resBg);
            for (int col = 0; col < 8; col++) {
                String bg = (lightBG) ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREY;
                lightBG = !lightBG;
                int loc = (whitePlayer) ? 7 - col : col;
                ChessPiece pos = board[wor][loc];
                String piece = (pos == null) ? ("   ") : drawPiece(pos);
                System.out.print(bg + piece + resBg);
            }
            System.out.print(borderStyle + " " + (wor+1) + " " + resBg);
            lightBG = !lightBG;
            System.out.print("\n");
        }
        System.out.print(borderStyle + "   ");
        for (int hcol = 0; hcol < 8; hcol++) {
            int hloc = whitePlayer ? 7 - hcol : hcol;
            System.out.print(" " + colLetters.get(hloc) + " ");
        }
        System.out.print("   " + resBg + "\n");
        System.out.print("\n");
    }

    public static void main(String[] args) {
        String authToken = "";
        String username = "";
        HashSet<FacadeGameData> facadeGames = new HashSet<>();
        String helpStr = (EscapeSequences.SET_TEXT_COLOR_GREEN + "help" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.printf("♕ 240 Chess Client: Type %s to get started\n", helpStr);



        while(true) {
            while (Objects.equals(authToken, "")) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "[LOGGED OUT] " + EscapeSequences.RESET_TEXT_COLOR + ">>> ");
                String userCommand = scanner.nextLine();

                String[] parsed = userCommand.split("\\s+");
                switch (parsed[0].toLowerCase()) {
                    case "quit":
                        facade.quit();
                        break;
                    case "help":
                        facade.preHelp();
                        break;
                    case "register":
                        if (parsed.length != 4) {
                            System.out.println("""
                                    Follow this format for the register command:
                                    register <username> <password> <email>
                                    """);
                            break;
                        }
                        username = parsed[1];
                        String email = parsed[3];
                        UserData user = new UserData(username, parsed[2], email);
                        authToken = facade.register(user);
                        break;
                    case "login":
                        if (parsed.length != 3) {
                            System.out.println("""
                                    Follow this format for the login command:
                                    login <username> <password>""");
                            break;
                        }
                        username = parsed[1];
                        authToken = facade.login(username, parsed[2]);
                        break;
                    default:
                        System.out.println("ERROR: < " + parsed[0] + " > unknown command\nValid commands:");
                        facade.preHelp();
                        break;
                }
            }

            while (authToken.length() > 1) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN + "[LOGGED IN] " + EscapeSequences.RESET_TEXT_COLOR + ">>> ");

                String userCommand = scanner.nextLine();
                String[] parsed = userCommand.split("\\s+");

                switch (parsed[0].toLowerCase()) {
                    case "help":
                        facade.postHelp();
                        break;
                    case "quit":
                        facade.logout(authToken);
                        facade.quit();
                        break;
                    case "logout":
                        if (parsed.length > 1) {
                            System.out.println("Do not provide any arguments with the logout command");
                            break;
                        }
                        facade.logout(authToken);
                        authToken = "";
                        break;
                    case "create":
                        if (parsed.length != 2) {
                            System.out.println(
                                    """
                                    Please follow this convention:
                                    create <game name>"""
                            );
                            break;
                        }
                        String gameName = parsed[1];
                        facade.createGame(authToken, gameName);
                        System.out.println("Game: <" + gameName + "> created");
                        facadeGames.clear();
                    case "list":
                        facadeGames = facade.listGames(authToken);
                        break;
                    case "join":
                        if (facadeGames.isEmpty()) {
                            System.out.println("Use the 'list' command before to this");
                            break;
                        }
                        try {
                            int gameSelectorID = Integer.parseInt(parsed[1]);
                            String teamColorSelector = parsed[2].toLowerCase();
                            if (!(teamColorSelector.equals("white")
                                    || teamColorSelector.equals("black"))) {
                                System.out.println("Please use either WHITE or BLACK to define your joining color.");
                                break;
                            }
                            boolean foundGame = false;
                            for (FacadeGameData facadeGame : facadeGames) {

                                if (facadeGame.selectorID != gameSelectorID) { continue; }
                                foundGame = true;

                                boolean userAlreadyJoinedCheck = (Objects.equals(username, facadeGame.blackUsername) ||
                                        Objects.equals(username, facadeGame.whiteUsername));
                                if (userAlreadyJoinedCheck) {
                                    String team = Objects.equals(username, facadeGame.blackUsername) ? "black" : "white";
                                    System.out.println("You have already joined this game as the " + team + " player");
                                    break;
                                }
                                facade.joinGame(facadeGame.gameID, facadeGame.selectorID, authToken, teamColorSelector);
                                ChessGame chessGame = new ChessGame();
                                drawBoards(chessGame, false);
                                drawBoards(chessGame, true);
                            }
                            if (!foundGame) {
                                System.out.println("That is not a valid game. Use 'list' to find valid game numbers");
                                break;
                            }


                        } catch (NumberFormatException e) {
                            System.out.println("Please provide a valid game number");
                        }
                        facadeGames.clear();
                        break;
                    case "observe":
                        int gameSelectorID = Integer.parseInt(parsed[1]);
                        System.out.print("Observing game number " + gameSelectorID);
                        ChessGame chessGame = new ChessGame();
                        drawBoards(chessGame, false);
                        drawBoards(chessGame, true);
                    default:
                        System.out.println("ERROR: < " + parsed[0] + " > unknown command\nValid commands:");
                        facade.postHelp();
                        break;
                }
            }
        }
    }
}