package execution;

import clientcommands.*;
import facade.FacadeGameData;
import facade.ServerFacade;
import ui.DrawPrompt;
import ui.EscapeSequences;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;

public class ClientExecution {

    private static ClientExecution instance;
    public static final int port = 8080;
    public static final ServerFacade FACADE = new ServerFacade(port);
    public static final Scanner SCANNER = new Scanner(System.in);

    public String authToken = "";
    public String username = "";
    public String gamePlayGameName = "";
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

        while(true) {
            while (Objects.equals(authToken, "")) {
                preLoginRun();
            }
            while (authToken.length() > 1) {
                postLoginRun();
            }
        }
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

    private void gamePlayRun() {
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
}
