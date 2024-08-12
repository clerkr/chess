package clientcommands.gameplay;

import chess.ChessPosition;
import clientcommands.Command;
import clientcommands.gameplay.exceptions.ColumnLetterFormatException;
import clientcommands.gameplay.exceptions.CoordinateFormatException;
import execution.ClientExecution;
import facade.ServerFacade;
import ui.DrawChessBoard;

import java.util.Map;

public class HighlightMoveCommand implements Command {

    ClientExecution client = ClientExecution.getInstance();
    Map<String, Integer> colNumsFromLetters = DrawChessBoard.colNumsFromLetters;
    ServerFacade facade = ClientExecution.FACADE;


    @Override
    public void execute() {

            if (client.parsed.length != 2) {
                System.out.println("Incorrect argument arrangement\nUse 'help' for command guides");
                return;
            }

//            ChessPosition boardPos = parseCoordStringAsPos(client.parsed[1]);
            facade.drawHighlight(client.parsed[1]);


    }




    private ChessPosition parseCoordStringAsPos(String coord) throws CoordinateFormatException, NumberFormatException, ColumnLetterFormatException {

        if (coord.length() != 2) {throw new CoordinateFormatException("");}

        String startColLetter = coord.substring(0,1);
        if (!colNumsFromLetters.containsKey(startColLetter)) {
            System.out.println();
            throw new ColumnLetterFormatException("placeholder message");
        }
        int col = colNumsFromLetters.get(startColLetter) + 1;

        int row = Integer.parseInt(coord.substring(1, 2));
        if (row < 1 || row > 8) {throw new NumberFormatException("Row num out of range");}

        return new ChessPosition(row, col);
    }


}
