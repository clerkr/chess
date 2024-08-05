package clientcommands;

import execution.ClientExecution;

public class PreLoginHelpCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public PreLoginHelpCommand() {
    }

    @Override
    public void execute() {
        ClientExecution.FACADE.preHelp();
    }
}
