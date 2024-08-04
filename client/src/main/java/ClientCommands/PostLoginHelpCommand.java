package ClientCommands;

import Execution.ClientExecution;
import Facade.ServerFacade;

public class PostLoginHelpCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public PostLoginHelpCommand() {}

    @Override
    public void execute() {
        ClientExecution.facade.postHelp();
    }
}
