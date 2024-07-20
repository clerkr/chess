package model;

public record JoinGameRequest(
        String authToken,
        String username,
        String playerColor,
        int gameID
) {}
