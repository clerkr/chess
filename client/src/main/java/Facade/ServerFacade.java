package Facade;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ServerFacade {

    private int port;

    public ServerFacade(int port) {
        this.port = port;
    }

    public void preHelp() {
        System.out.println(
                """
                help
                register <username> <password> <email>
                login <username> <password>
                quit
                """
        );
    }

    public void postHelp() {
        System.out.println(
                """
                help
                create <game name>
                join <game number> <WHITE | BLACK>
                list - lists all games
                logout
                quit
                """
        );
    }

    public String register(UserData newUser) {
        String authToken = "";
        try {
            URI uri = new URI("http://localhost:" + port + "/user");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            var body = Map.of(
                    "username", newUser.username(),
                    "password", newUser.password(),
                    "email", newUser.email());

            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            int statusCode = http.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    Map res = new Gson().fromJson(inputStreamReader, Map.class);
                    authToken = (String) res.get("authToken");
                }
            } else if (statusCode == 403) {
                System.out.println("ERROR: Username already in use");
            }


        } catch (URISyntaxException e) {
            System.out.println(e);
        } catch (ProtocolException | MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return authToken;
    }

    public String login(String username, String password) {
        String authToken = "";
        try {
            URI uri = new URI("http://localhost:" + port + "/session");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            var body = Map.of(
                    "username", username,
                    "password", password
            );

            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            int statusCode = http.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
//                System.out.println(new Gson().fromJson(inputStreamReader, Map.class));
                    Map res = new Gson().fromJson(inputStreamReader, Map.class);
                    authToken = (String) res.get("authToken");
                }
            } else if (statusCode == 401) {
                System.out.println("ERROR: Invalid username or password");
            }

        } catch (URISyntaxException e) {
            System.out.println(e);
        } catch (ProtocolException | MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return authToken;
    }

    public void quit() {
        System.out.println("Closing chess 240...");
        System.exit(0);
    }

    public void logout(String authToken) {
        try {
            URI uri = new URI("http://localhost:" + port + "/session");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("DELETE");
            http.addRequestProperty("Authorization", authToken);

            http.connect();
            int statusCode = http.getResponseCode();
            if (statusCode == 401) {
                System.out.println("ERROR: Account issue. Consider restarting the program");
            }

        } catch (URISyntaxException e) {
            System.out.println(e);
        } catch (ProtocolException | MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createGame(String authToken, String gameName) {
        try {
            URI uri = new URI("http://localhost:" + port + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Content-Type", "application/json");
            var body = Map.of(
                    "gameName", gameName
            );

            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            int statusCode = http.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    Map res = new Gson().fromJson(inputStreamReader, Map.class);
                }
            } else if (statusCode == 401) {
                System.out.println("ERROR: Could not create game");
            }

        } catch (URISyntaxException e) {
            System.out.println(e);
        } catch (ProtocolException | MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashSet<FacadeGameData> listGames(String authToken) {
        HashSet<FacadeGameData> facadeGames = new HashSet<FacadeGameData>();
        try {
            URI uri = new URI("http://localhost:" + port + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("GET");
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Content-Type", "application/json");


            int statusCode = http.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    Map res = new Gson().fromJson(inputStreamReader, Map.class);
                    List<Map<String, Object>> games = (List<Map<String, Object>>) res.get("games");
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
                        System.out.printf("| %-4s | %-15s | %-20s | %-20s |\n", counter, gameName, whiteUsername, blackUsername);
                        counter++;
                    }
                    System.out.println("+------+-----------------+----------------------+----------------------+");

                }
            } else if (statusCode == 401) {
                System.out.println("ERROR: Could not create game");
            }

        } catch (URISyntaxException e) {
            System.out.println(e);
        } catch (ProtocolException | MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return facadeGames;
    }

    public void joinGame(int gameID, int selectorID, String authToken, String playerColor) {
        try {
            URI uri = new URI("http://localhost:" + port + "/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("PUT");
            http.setDoOutput(true);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Content-Type", "application/json");
            var body = Map.of(
                    "gameID", gameID,
                    "playerColor", playerColor.toUpperCase()
            );

            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            int statusCode = http.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (InputStream respBody = http.getInputStream()) {
                    InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                    Map res = new Gson().fromJson(inputStreamReader, Map.class);
                    System.out.println("Joined " + selectorID);
                }
            } else if (statusCode == 403) {
                System.out.println("ERROR: That color is already taken by another player in this game");
            }

        } catch (URISyntaxException e) {
            System.out.println(e);
        } catch (ProtocolException | MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
