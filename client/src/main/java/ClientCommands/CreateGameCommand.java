package ClientCommands;

import Execution.ClientExecution;

public class CreateGameCommand implements Command{

    ClientExecution client = ClientExecution.getInstance();

    public CreateGameCommand() {}

    @Override
    public void execute() {
        if (client.parsed.length != 2) {
            System.out.println(
                    """
                    Please follow this convention:
                    create <game name>"""
            );

        } else {
            String gameName = client.parsed[1];
            ClientExecution.facade.createGame(client.authToken, gameName);
            System.out.println("Game: <" + gameName + "> created");
            client.facadeGames.clear();
        }
    }
}


