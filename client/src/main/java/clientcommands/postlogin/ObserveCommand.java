package clientcommands.postlogin;

import clientcommands.Command;
import execution.ClientExecution;
import facade.FacadeGameData;

import java.util.Objects;

public class ObserveCommand implements Command {

    ClientExecution client = ClientExecution.getInstance();

    public ObserveCommand() {}

//    @Override
//    public void execute() {
//        if (client.parsed.length != 2) {
//            System.out.println("Please use 'list' to get a game number to observe");
//            return;
//        }
//        try {
//            int gameSelectorID = Integer.parseInt(client.parsed[1]);
//            return;
//        } catch (NumberFormatException e) {
//            System.out.println("Please provide a valid game number");
//            return;
//        }
//    }

    @Override
    public void execute() {
        if (client.facadeGames.isEmpty()) {
            System.out.println("Use the 'list' command before to this");
            return;
        }
        if (client.parsed.length != 2) {
            System.out.println("Incorrect argument arrangment\nUse 'help' for command guides");
            return;
        }
        try {
            int gameSelectorID = Integer.parseInt(client.parsed[1]);
            if (gameSelectorID < 1 || gameSelectorID > client.facadeGames.size()) {
                System.out.println("That is not a valid game id. Use 'list' to find valid game numbers");
                return;
            }

            FacadeGameData facadeGame = findGame(gameSelectorID);
            if (facadeGame == null) {
                System.out.println("Could not find game");
                return;
            }

            ClientExecution.FACADE.observeGame(facadeGame.gameID, facadeGame.selectorID, client.authToken);
            client.gamePlayGameName = facadeGame.gameName;
            client.gamePlayGameID = facadeGame.gameID;
            if (!checkPlayerInGame(facadeGame)){
                client.observing = true;
            }

        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid game number");
        }
    }

    private boolean checkPlayerInGame(FacadeGameData facadeGameData) {
        return Objects.equals(client.username, facadeGameData.blackUsername) ||
                Objects.equals(client.username, facadeGameData.whiteUsername);
    }

    private FacadeGameData findGame(int gameSelectorID) {
        FacadeGameData game = null;
        for (FacadeGameData facadeGame : client.facadeGames) {
            if (facadeGame.selectorID != gameSelectorID) {
                continue;
            }
            game = facadeGame;
        }
        return game;

    }

}


