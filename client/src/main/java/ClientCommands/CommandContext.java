package ClientCommands;

import Facade.FacadeGameData;
import Facade.ServerFacade;

import java.util.HashSet;
import java.util.Scanner;

public class CommandContext {

    private Scanner scanner;
    private ServerFacade facade;
    private String authToken;
    private String username;
    private HashSet<FacadeGameData> facadeGames;

    public CommandContext(
            Scanner scanner,
            ServerFacade facade,
            String authToken,
            String username,
            HashSet<FacadeGameData> facadeGames
    ) {
        this.scanner = scanner;
        this.facade = facade;
        this.authToken = authToken;
        this.username = username;
        this.facadeGames = facadeGames;
    }

    // Getters
    public Scanner getScanner() {
        return scanner;
    }

    public ServerFacade getFacade() {
        return facade;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public HashSet<FacadeGameData> getFacadeGames() {
        return facadeGames;
    }
}
