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
    private String[] parsed;

    public CommandContext(
            Scanner scanner,
            ServerFacade facade,
            String authToken,
            String username,
            HashSet<FacadeGameData> facadeGames,
            String[] parsed
    ) {
        this.scanner = scanner;
        this.facade = facade;
        this.authToken = authToken;
        this.username = username;
        this.facadeGames = facadeGames;
        this.parsed = parsed;
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

    public void setAuthToken(String newAuthToken) {
        this.authToken = newAuthToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public HashSet<FacadeGameData> getFacadeGames() {
        return facadeGames;
    }

    public void clearFacadeGames() {
        facadeGames.clear();
    }

    public void setFacadeGames(HashSet<FacadeGameData> newFacadeGames) {
        this.facadeGames = newFacadeGames;
    }

    public String[] getParsed() {
        return parsed;
    }
}
