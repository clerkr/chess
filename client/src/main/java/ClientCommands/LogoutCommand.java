package ClientCommands;

import Execution.ClientExecution;
import model.UserData;

public class LogoutCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public LogoutCommand() {}

    @Override
    public void execute() {
        ClientExecution.facade.logout(client.authToken);
        client.authToken = "";
    }
}

