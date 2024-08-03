package Facade;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;

public class ServerFacade {
    public static void main(String[] args){
//        help();
        register();
        quit();
    }

    private static void help() {
        System.out.println(
                """
                register <username> <password> <email>
                login <username> <password>
                quit
                help
                """
        );
    }

    private static void register() {
        try {
            URI uri = new URI("http://localhost:8080/user");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            var body = Map.of(
                    "username", "user1",
                    "password", "secretpass",
                    "email", "useremail@fake.com");
//            http.connect();

            try (var outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(body);
                outputStream.write(jsonBody.getBytes());
            }

            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                System.out.println(new Gson().fromJson(inputStreamReader, Map.class));
            }

        } catch (URISyntaxException e) {
            System.out.println(e);
        } catch (ProtocolException | MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void quit() {
        System.out.println("Closing chess 240...");
        System.exit(0);
    }

    private static void listGames() {
        try {
            URI uri = new URI("http://localhost:8080/game");
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod("GET");

            http.connect();

            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                System.out.println(new Gson().fromJson(inputStreamReader, Map.class));
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
