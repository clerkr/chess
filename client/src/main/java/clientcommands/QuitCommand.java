package clientcommands;

import execution.ClientExecution;

public class QuitCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public QuitCommand () {}

    @Override
    public void execute() {
        ClientExecution.FACADE.quit();
    }
}
