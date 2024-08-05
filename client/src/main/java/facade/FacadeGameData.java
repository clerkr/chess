package facade;

import chess.ChessGame;

public class FacadeGameData {
    public int selectorID;
    public int gameID;
    public String gameName;
    public String whiteUsername;
    public String blackUsername;
    public ChessGame gameObj;
    public FacadeGameData(
            int selectorID,
            int gameID,
            String gameName,
            String whiteUsername,
            String blackUsername,
            ChessGame gameObj
    ) {
        this.selectorID = selectorID;
        this.gameID = gameID;
        this.gameName = gameName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameObj = gameObj;
    }

}
