package ClientCommands;

import Execution.ClientExecution;
import Facade.FacadeGameData;
import chess.ChessGame;
import ui.DrawChessBoard;

import java.util.Objects;

public class JoinGameCommand implements Command{

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
            if (gameSelectorID < 1 || gameSelectorID >= client.facadeGames.size()) {
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
            if (joinedCheck(facadeGame)) { return; }
            ClientExecution.facade.joinGame(facadeGame.gameID, facadeGame.selectorID, client.authToken, teamColorSelector);

            DrawChessBoard.drawBoards(new ChessGame());
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
            System.out.println("You have already joined this game as the " + team + " player");
            return true;
        }
        return false;
    }
}


