package clientcommands.postlogin;

import clientcommands.Command;
import execution.ClientExecution;

public class PostLoginHelpCommand implements Command {

    ClientExecution client = ClientExecution.getInstance();

    public PostLoginHelpCommand() {}

    @Override
    public void execute() {
        ClientExecution.FACADE.postHelp();
    }
}
