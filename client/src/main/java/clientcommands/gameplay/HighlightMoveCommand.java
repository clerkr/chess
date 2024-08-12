package clientcommands.gameplay;

import clientcommands.Command;
import execution.ClientExecution;
import facade.ServerFacade;
import ui.DrawChessBoard;

import java.util.Map;

public class HighlightMoveCommand implements Command {

    ClientExecution client = ClientExecution.getInstance();
    ServerFacade facade = ClientExecution.FACADE;


    @Override
    public void execute() {

            if (client.parsed.length != 2) {
                System.out.println("Incorrect argument arrangement\nUse 'help' for command guides");
                return;
            }
            facade.drawHighlightHandler(client.parsed[1]);
    }
}
