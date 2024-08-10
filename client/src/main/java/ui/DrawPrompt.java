package ui;

import execution.ClientExecution;

public class DrawPrompt {

    static ClientExecution client = ClientExecution.getInstance();

    public static void drawLoggedInPrompt() {
        System.out.print(
                "\n" +
                EscapeSequences.SET_TEXT_COLOR_GREEN + "[LOGGED IN] " + EscapeSequences.RESET_TEXT_COLOR +
                ">>> ");
    }

    public static void drawLoggedOutPrompt() {
        System.out.print(
                "\n" +
                EscapeSequences.SET_TEXT_COLOR_RED + "[LOGGED OUT] " + EscapeSequences.RESET_TEXT_COLOR +
                ">>> ");
    }

    public static void drawGamePlayPrompt() {

        String drawObserving = "";
        if (client.observing) {
            drawObserving = "observing: ";
        }


        System.out.print(
                "\n" +
                EscapeSequences.SET_TEXT_COLOR_BLUE + "[" + drawObserving + client.gamePlayGameName +"] " + EscapeSequences.RESET_TEXT_COLOR +
                ">>> ");
    }

    public static void printNotification(String message) {
        System.out.print(
                "\n" +
                EscapeSequences.SET_TEXT_COLOR_YELLOW +
                        "(?!) " +
                EscapeSequences.RESET_TEXT_COLOR +
                        message
        );
    }
}
