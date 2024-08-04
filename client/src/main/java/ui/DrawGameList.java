package ui;

import Facade.FacadeGameData;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class DrawGameList {

    List<Map<String, Object>> games;
    HashSet<FacadeGameData> facadeGames;



    public static void drawTable(List<Map<String, Object>> games,
                                 HashSet<FacadeGameData> facadeGames) {
        int counter = 1;
        System.out.println("+------+-----------------+----------------------+----------------------+");
        System.out.printf("| %-4s | %-15s | %-20s | %-20s |\n", "NO. ", "GAMENAME", "WHITE", "BLACK");
        System.out.println("+------+-----------------+----------------------+----------------------+");
        for (Map<String, Object> game : games) {
            Double gameID = (Double) game.get("gameID");
            String gameName = (String) game.get("gameName");
            String whiteUsername = (String) game.getOrDefault("whiteUsername", "-");
            String blackUsername = (String) game.getOrDefault("blackUsername", "-");

            Gson gson = new GsonBuilder().create();
            String chessGameJson = gson.toJson(game.get("game"));
            ChessGame gameObj = gson.fromJson(chessGameJson, ChessGame.class);

            facadeGames.add(new FacadeGameData(counter, gameID.intValue(), gameName, whiteUsername, blackUsername, gameObj));
            System.out.printf("| %-4s | %-15s | %-20s | %-20s |\n",
                    counter,
                    (gameName.length() > 15 ? (gameName.substring(0,11) + "...") : gameName),
                    (whiteUsername.length() > 20 ? (whiteUsername.substring(0,16) + "...") : whiteUsername),
                    (blackUsername.length() > 20 ? (blackUsername.substring(0,16) + "...") : blackUsername)
            );
            counter++;
        }
        System.out.println("+------+-----------------+----------------------+----------------------+");
    }

}
