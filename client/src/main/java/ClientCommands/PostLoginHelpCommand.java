package ClientCommands;

import Facade.ServerFacade;

public class PostLoginHelpCommand implements Command{

    ServerFacade facade;
    public PostLoginHelpCommand(CommandContext context) {
        this.facade = context.getFacade();
    }

    @Override
    public void execute() {
        facade.postHelp();
    }
}
