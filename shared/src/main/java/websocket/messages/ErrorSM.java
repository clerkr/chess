package websocket.messages;

import com.google.gson.Gson;

public class ErrorSM extends ServerMessage{

    private String errorMessage;

    public ErrorSM(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    public static ErrorSM prepareErrorSM(Exception e) {
        String errorMessage = "ERROR: " + e.toString();
        return new ErrorSM(
                ServerMessageType.ERROR,
                errorMessage
        );
    }

}
