package clientcommands.postlogin;

import clientcommands.Command;
import execution.ClientExecution;
import facade.FacadeGameData;

import java.util.Objects;


public class JoinGameCommand implements Command {

    ClientExecution client = ClientExecution.getInstance();



    public JoinGameCommand() {}

    @Override
    public void execute() {
        if (client.facadeGames.isEmpty()) {
            System.out.println("Use the 'list' command before to this");
            return;
        }
        if (client.parsed.length != 3) {
            System.out.println("Incorrect argument arrangment\nUse 'help' for command guides");
            return;
        }
        try {
            int gameSelectorID = Integer.parseInt(client.parsed[1]);
            if (gameSelectorID < 1 || gameSelectorID > client.facadeGames.size()) {
                System.out.println("That is not a valid game id. Use 'list' to find valid game numbers");
                return;
            }
            String teamColorSelector = client.parsed[2].toLowerCase();
            if (!(teamColorSelector.equals("white")
                    || teamColorSelector.equals("black"))) {
                System.out.println("Please use either WHITE or BLACK to define your joining color.");
                return;
            }

            FacadeGameData facadeGame = findGame(gameSelectorID);
            if (facadeGame == null) {
                System.out.println("Could not find game");
                return;
            }
            if (joinedCheck(facadeGame)) {
                ClientExecution.FACADE.observeGame(facadeGame.gameID, facadeGame.selectorID, client.authToken);
                client.gamePlayGameName = facadeGame.gameName;
                client.gamePlayGameID = facadeGame.gameID;
            } else {
                boolean teamFilled = filledTeamCheck(facadeGame, teamColorSelector);
                if (teamFilled) {
                    System.out.println("ERROR: That color is already taken by another player in this game");
                } else {

                    boolean joined = ClientExecution.FACADE.joinGame(facadeGame.gameID, facadeGame.selectorID, client.authToken, teamColorSelector);
                    if (joined) {
                        client.gamePlayGameName = facadeGame.gameName;
                        client.gamePlayGameID = facadeGame.gameID;
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid game number");
        }
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

    private boolean joinedCheck(FacadeGameData facadeGame) {
        boolean userAlreadyJoinedCheck = (Objects.equals(client.username, facadeGame.blackUsername) ||
                Objects.equals(client.username, facadeGame.whiteUsername));
        if (userAlreadyJoinedCheck) {
            String team = Objects.equals(client.username, facadeGame.blackUsername) ? "black" : "white";
            System.out.println("You are the " + team + " player");
            return true;
        }
        return false;
    }

    private boolean filledTeamCheck(FacadeGameData facadeGame, String colorSelector) {

        boolean whiteIsFilled = !Objects.equals(facadeGame.whiteUsername, "-");
        boolean blackIsFilled = !Objects.equals(facadeGame.blackUsername, "-");

        return Objects.equals(colorSelector, "white") ?
                whiteIsFilled :
                blackIsFilled;
    }
}


