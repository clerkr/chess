package execution;

import chess.ChessGame;
import chess.ChessPosition;
import clientcommands.*;
import clientcommands.gameplay.LeaveCommand;
import clientcommands.gameplay.MakeMoveCommand;
import clientcommands.gameplay.ResignCommand;
import clientcommands.postlogin.*;
import clientcommands.prelogin.LoginCommand;
import clientcommands.prelogin.PreLoginHelpCommand;
import clientcommands.prelogin.RegisterCommand;
import facade.FacadeGameData;
import facade.ServerFacade;
import ui.DrawChessBoard;
import ui.DrawPrompt;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

public class ClientExecution {

    private static ClientExecution instance;
    public static final int PORT = 8080;
    public static final ServerFacade FACADE = new ServerFacade(PORT);
    public static final Scanner SCANNER = new Scanner(System.in);

    public String authToken = "";
    public String username = "";
    public String gamePlayGameName = "";
    public int gamePlayGameID = -1;
    public boolean observing = false;
    public HashSet<FacadeGameData> facadeGames = new HashSet<>();
    public String[] parsed = new String[10];


    private ClientExecution() {}
    public static synchronized ClientExecution getInstance() {
        if (instance == null) {
            instance = new ClientExecution();
        }
        return instance;
    }

    public void run() {
        DrawChessBoard.drawHighlightedBoard(new ChessGame(), true, new ChessPosition(7,5));
//        while(true) {
//            while (Objects.equals(authToken, "")) {
//                preLoginRun();
//            }
//            while (authToken.length() > 1) {
//                postLoginRun();
//            }
//        }
    }

    private void preLoginRun() {
        DrawPrompt.drawLoggedOutPrompt();
        String userCommand = SCANNER.nextLine();
        parsed = userCommand.split("\\s+");
        switch (parsed[0].toLowerCase()) {
            case "quit":
                new QuitCommand().execute(); break;
            case "help":
                new PreLoginHelpCommand().execute(); break;
            case "register":
                new RegisterCommand().execute(); break;
            case "login":
                new LoginCommand().execute(); break;
            default:
                System.out.println("ERROR: < " + parsed[0] + " > unknown command\nValid commands:");
                FACADE.preHelp();
                break;
        }
    }

    private void postLoginRun() {
//        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN + "[LOGGED IN] " + EscapeSequences.RESET_TEXT_COLOR + ">>> ");
        while(!gamePlayGameName.isEmpty()) {
            gamePlayRun();
        }
        DrawPrompt.drawLoggedInPrompt();
        String userCommand = SCANNER.nextLine();
        parsed = userCommand.split("\\s+");
        switch (parsed[0].toLowerCase()) {
            case "help":
                new PostLoginHelpCommand().execute();
                break;
            case "quit":
                new LogoutCommand().execute();
                new QuitCommand().execute();
                break;
            case "logout":
                new LogoutCommand().execute();
                break;
            case "create":
                new CreateGameCommand().execute();
            case "list":
                facadeGames = FACADE.listGames(authToken);
                break;
            case "join":
                new JoinGameCommand().execute();
                facadeGames.clear();
                break;
            case "observe":
                new ObserveCommand().execute();
                break;
            default:
                System.out.println("ERROR: < " + parsed[0] + " > unknown command\nValid commands:");
                FACADE.postHelp();
                break;
        }
    }

    // Just a dupe rn... add in other commands
    private void gamePlayRun() {
        DrawPrompt.drawGamePlayPrompt();
        String userCommand = SCANNER.nextLine();
        parsed = userCommand.split("\\s+");
        switch (parsed[0].toLowerCase()) {
            case "help":
                System.out.println(
                        """
                        draw - redraws the current state of the game's board
                        leave - return from gameplay
                        move [start coordinate] [end coordinate] - coordinates should be provided as column letter-row number pairs (e.g. 'a1,' 'g5,' or 'c8'
                        moves [piece coordinate] - highlights valid moves for the piece on the provided coordinate
                        resign - forfeiture (confirmation is prompted)"""
                );
                break;
            case "leave":
//                gamePlayGameName = "";
                new LeaveCommand().execute();
                observing = false;
                break;
            case "draw":
                FACADE.drawBoardHandler();
                break;
            case "move":
                if (observing) {
                    System.out.println("Observers cannot move pieces");
                    break;
                }
                new MakeMoveCommand().execute();
                break;
            case "resign":
                if (observing) {
                    System.out.println("Observers cannot resign from the game");
                    break;
                }
                new ResignCommand().execute();
                break;
            default:
                System.out.println("ERROR: < " + parsed[0] + " > unknown command\nValid commands:");
                System.out.println(
                        """
                        draw - redraws the current state of the game's board
                        leave - return from gameplay
                        move [start coordinate] [end coordinate] - coordinates should be provided as column letter-row number pairs (e.g. 'a1,' 'g5,' or 'c8'
                        moves [piece coordinate] - highlights valid moves for the piece on the provided coordinate
                        resign - forfeiture (confirmation is prompted)"""
                );
                break;
        }
    }
}
