package ClientCommands;

import Facade.ServerFacade;

public class PreLoginHelpCommand implements Command{

    ServerFacade facade;
    public PreLoginHelpCommand(CommandContext context) {
        this.facade = context.getFacade();
    }

    @Override
    public void execute() {
        facade.preHelp();
    }
}
