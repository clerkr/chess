package websocket.messages;

public class ErrorSM extends ServerMessage{

    String errorMessage;

    public ErrorSM(ServerMessageType type, String message) {
        super(type, message);
        this.errorMessage = message; //!!!!
    }
}
