package clientcommands.gameplay;

import clientcommands.Command;
import execution.ClientExecution;
import facade.ServerFacade;
import ui.EscapeSequences;

import java.util.Objects;

import static execution.ClientExecution.SCANNER;

public class ResignCommand implements Command {

    ClientExecution client = ClientExecution.getInstance();
    ServerFacade facade = ClientExecution.FACADE;

    @Override
    public void execute() {
        if (client.parsed.length != 1) {
            System.out.println("Incorrect argument arrangment\nUse 'help' for command guides");
            return;
        }

        System.out.println(
                EscapeSequences.SET_TEXT_COLOR_MAGENTA +
                        "Are you sure you want to resign? " +
                EscapeSequences.RESET_TEXT_COLOR +
                        "Type 'yes' to confirm: ");
        String resignConfirm = SCANNER.nextLine();

        if (Objects.equals(resignConfirm.strip().toLowerCase(), "yes")) {
            facade.resignGameHandler(client.authToken, client.gamePlayGameID);
        } else {
            System.out.println("Resignation cancelled");
            facade.drawBoardHandler();
        }

    }
}
