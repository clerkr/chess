package ClientCommands;

import Execution.ClientExecution;
import model.UserData;

public class LogoutCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public LogoutCommand() {}

    @Override
    public void execute() {
        if (client.parsed.length != 3) {
            System.out.println(
                """
                Follow this format for the login command:
                login <username> <password>""");
        } else {
            client.username = client.parsed[1];
            String password = client.parsed[2];
            client.authToken = ClientExecution.facade.login(client.username, password);
        }
    }
}

