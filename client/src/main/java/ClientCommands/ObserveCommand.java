package ClientCommands;

import Execution.ClientExecution;
import chess.ChessGame;
import ui.DrawChessBoard;

public class ObserveCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public ObserveCommand() {}

    @Override
    public void execute() {
        if (client.parsed.length != 2) {
            System.out.println("Please use 'list' to get a game number to observe");
            return;
        }
        try {
            int gameSelectorID = Integer.parseInt(client.parsed[1]);
            System.out.println("Observing game number " + gameSelectorID);
            ChessGame chessGame = new ChessGame();
            DrawChessBoard.drawBoards(chessGame);
            return;
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid game number");
            return;
        }
    }

}


