package ClientCommands;

import Execution.ClientExecution;
import Facade.ServerFacade;
import model.UserData;

public class RegisterCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public RegisterCommand() {}

    @Override
    public void execute() {
        if (client.parsed.length != 4) {
            System.out.println(
                """
                Follow this format for the register command:
                register <username> <password> <email>
                """
            );
        } else {
            client.username = (client.parsed[1]);
            String password = client.parsed[2];
            String email = client.parsed[3];
            UserData user = new UserData(client.username, password, email);
            client.authToken = ClientExecution.facade.register(user);
        }
    }
}
