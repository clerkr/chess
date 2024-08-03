import Facade.ServerFacade;
import chess.*;
import model.UserData;
import ui.EscapeSequences;

import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static ServerFacade facade = new ServerFacade(8080);
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String authToken = "";
        String username = "";
        String helpStr = (EscapeSequences.SET_TEXT_COLOR_GREEN + "help" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.printf("♕ 240 Chess Client: Type %s to get started\n", helpStr);



        while(true) {
            while (Objects.equals(authToken, "")) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "[LOGGED OUT] " + EscapeSequences.RESET_TEXT_COLOR + ">>> ");
                String userCommand = scanner.nextLine();
                String[] parsed = userCommand.split("\\s+");

                switch (parsed[0]) {
                    case "quit":
                        facade.quit();
                        break;
                    case "help":
                        facade.help();
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
                        System.out.println("ERROR: < " + parsed[0] + " > unknown command");
                }
            }

            while (authToken.length() > 1) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN + "[LOGGED IN] " + EscapeSequences.RESET_TEXT_COLOR + ">>> ");

                String userCommand = scanner.nextLine();
                String[] parsed = userCommand.split("\\s+");

                switch (parsed[0]) {
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
                    case "list":
                        facade.listGames(authToken);
                }
            }
        }
    }
}