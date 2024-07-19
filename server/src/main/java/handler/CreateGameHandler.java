package handler;
import spark.Request;
import spark.Response;
import spark.Route;

/*
The Handler classes are intended to convert HTTP to Java.
Send the converted information from the HTTP to the Server classes
 */

public class CreateGameHandler implements Route {


    public CreateGameHandler() {}

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }

//    public Object handle() {
//        var body = req.body();
//        var header = req.headers();
//    }

}
