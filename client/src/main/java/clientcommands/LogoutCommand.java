package clientcommands;

import execution.ClientExecution;

public class LogoutCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public LogoutCommand() {}

    @Override
    public void execute() {
        ClientExecution.FACADE.logout(client.authToken);
        client.authToken = "";
    }
}

