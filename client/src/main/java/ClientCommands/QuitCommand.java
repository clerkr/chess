package ClientCommands;

import Execution.ClientExecution;
import Facade.ServerFacade;

public class QuitCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public QuitCommand () {}

    @Override
    public void execute() {
        ClientExecution.facade.quit();
    }
}
