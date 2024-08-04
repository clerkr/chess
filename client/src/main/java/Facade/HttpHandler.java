package Facade;

import com.google.gson.Gson;
import org.glassfish.tyrus.core.wsadl.model.Endpoint;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpHandler {

    public enum RequestMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    private final String url = "http://localhost:";
    private final int port;
    private final String reqMethod;
    private final String endpoint;
    private final String authToken;
    private Map<String, String> body = new HashMap<>();

    public HttpHandler(int port,
                       String reqMethod,
                       String endpoint, String authToken) {
        this.port = port;
        this.reqMethod = reqMethod;
        this.endpoint = endpoint;
        this.authToken = authToken;
    }

    // Overloaded constructor
    public HttpHandler(int port,
                       String reqMethod,
                       String endpoint, String authToken,
                       Map<String, String> body) {
        this.port = port;
        this.reqMethod = reqMethod;
        this.endpoint = endpoint;
        this.authToken = authToken;
        this.body = body;
    }

    public HttpURLConnection establish() {
        try {
            URI uri = new URI(url + port + "/" + endpoint);
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(reqMethod);
            if (Objects.equals(reqMethod, "POST") ||
                    Objects.equals(reqMethod, "PUT")) {
                http.setDoOutput(true);
            }
            http.addRequestProperty("Content-Type", "application/json");
            http.addRequestProperty("Authorization", authToken);
            return http;
        } catch (URISyntaxException e) {
            System.out.println(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void runOutputStream(HttpURLConnection http) {
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
