package clientcommands.gameplay;

import clientcommands.Command;
import execution.ClientExecution;
import facade.FacadeGameData;

public class LeaveCommand implements Command {

    ClientExecution client = ClientExecution.getInstance();


    @Override
    public void execute() {
        try {
            ClientExecution.FACADE.leaveGame(client.authToken, client.gamePlayGameID);
            client.gamePlayGameName = "";
            client.gamePlayGameID = -1;
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid game number");
        }

    }

}
