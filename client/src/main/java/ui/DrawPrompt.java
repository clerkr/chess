package ui;

public class DrawPrompt {

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
}
