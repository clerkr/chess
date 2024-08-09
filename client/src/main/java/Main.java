import execution.ClientExecution;
import facade.ServerFacade;
import ui.EscapeSequences;

import java.util.Scanner;

public class Main {

    private static final ServerFacade FACADE = new ServerFacade(8080);
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Welcome to Chess 240!\nType " +
                EscapeSequences.SET_TEXT_COLOR_GREEN + "help" + EscapeSequences.RESET_TEXT_COLOR +
                " for a list of commands");
        ClientExecution client = ClientExecution.getInstance();
        client.run();
    }
}