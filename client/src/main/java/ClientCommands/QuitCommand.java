package ClientCommands;

import Facade.ServerFacade;

public class QuitCommand implements Command{

    ServerFacade facade;
    public QuitCommand (CommandContext context) {
        this.facade = context.getFacade();
    }

    @Override
    public void execute() {
        facade.quit();
    }
}
