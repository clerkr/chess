package ClientCommands;

import Facade.ServerFacade;
import model.UserData;

public class RegisterCommand implements Command{
    ServerFacade facade;
    UserData userData;
    public RegisterCommand (ServerFacade facade, UserData userData) {
        this.facade = facade;
        this.userData = userData;
    }

    @Override
    public void execute() {
        facade.register(userData);
    }
}
