package model;

import java.util.ArrayList;
import java.util.HashSet;

public record ListGamesResult(
        HashSet<GameData> games
) {}
