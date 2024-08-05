import execution.ClientExecution;
import facade.ServerFacade;
import ui.EscapeSequences;

import java.util.Scanner;

public class Main {

    private static final ServerFacade facade = new ServerFacade(8080);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Chess 240!\nType " +
                EscapeSequences.SET_TEXT_COLOR_GREEN + "help" + EscapeSequences.RESET_TEXT_COLOR +
                " for a list of commands");
        ClientExecution client = ClientExecution.getInstance();
        client.run();
    }
}