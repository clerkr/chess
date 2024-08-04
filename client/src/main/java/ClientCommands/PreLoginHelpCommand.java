package ClientCommands;

import Execution.ClientExecution;
import Facade.ServerFacade;

public class PreLoginHelpCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public PreLoginHelpCommand() {
    }

    @Override
    public void execute() {
        ClientExecution.facade.preHelp();
    }
}
