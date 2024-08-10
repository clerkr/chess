import execution.ClientExecution;
import ui.EscapeSequences;

public class Main {


    public static void main(String[] args) {
        System.out.print("Welcome to Chess 240!\nType " +
                EscapeSequences.SET_TEXT_COLOR_GREEN + "help" + EscapeSequences.RESET_TEXT_COLOR +
                " for a list of commands");
        ClientExecution client = ClientExecution.getInstance();
        client.run();
    }
}