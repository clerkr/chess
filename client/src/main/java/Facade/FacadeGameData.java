package Facade;

public class FacadeGameData {
    public int selectorID;
    public int gameID;
    public String gameName;
    public String whiteUsername;
    public String blackUsername;

    public FacadeGameData(
            int selectorID,
            int gameID,
            String gameName,
            String whiteUsername,
            String blackUsername
    ) {
        this.selectorID = selectorID;
        this.gameID = gameID;
        this.gameName = gameName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
    }

}
