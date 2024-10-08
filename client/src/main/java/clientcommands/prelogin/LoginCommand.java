package clientcommands.prelogin;

import clientcommands.Command;
import execution.ClientExecution;

public class LoginCommand implements Command {

    ClientExecution client = ClientExecution.getInstance();

    public LoginCommand() {}

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
            client.authToken = ClientExecution.FACADE.login(client.username, password);
        }
    }
}


