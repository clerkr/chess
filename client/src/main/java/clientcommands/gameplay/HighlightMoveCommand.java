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




}
