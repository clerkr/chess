import ClientCommands.CommandContext;
import ClientCommands.PostLoginHelpCommand;
import ClientCommands.PreLoginHelpCommand;
import ClientCommands.QuitCommand;
import Facade.FacadeGameData;
import Facade.ServerFacade;
import chess.ChessGame;
import model.UserData;
import ui.DrawChessBoard;
import ui.EscapeSequences;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static final ServerFacade facade = new ServerFacade(8080);
    private static final Scanner scanner = new Scanner(System.in);

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

                CommandContext context = new CommandContext(
                        scanner,
                        facade,
                        authToken,
                        username,
                        facadeGames
                );

                switch (parsed[0].toLowerCase()) {
                    case "quit":
                        new QuitCommand(context).execute();
                        break;
                    case "help":
                        new PreLoginHelpCommand(context).execute();
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
                        String password = parsed[2];
                        String email = parsed[3];
                        UserData user = new UserData(username, password, email);
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

                CommandContext context = new CommandContext(
                        scanner,
                        facade,
                        authToken,
                        username,
                        facadeGames
                );

                switch (parsed[0].toLowerCase()) {
                    case "help":
                        new PostLoginHelpCommand(context).execute();
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
                                DrawChessBoard.drawBoards(chessGame);
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
                        if (parsed.length != 2) {
                            System.out.println("Please use 'list' to get a game number to observe");
                            break;
                        }
                        try {
                            int gameSelectorID = Integer.parseInt(parsed[1]);
                            System.out.println("Observing game number " + gameSelectorID);
                            ChessGame chessGame = new ChessGame();
                            DrawChessBoard.drawBoards(chessGame);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Please provide a valid game number");
                            break;
                        }
                    default:
                        System.out.println("ERROR: < " + parsed[0] + " > unknown command\nValid commands:");
                        facade.postHelp();
                        break;
                }
            }
        }
    }
}