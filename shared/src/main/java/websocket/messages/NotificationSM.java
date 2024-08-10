package websocket.messages;

public class NotificationSM extends ServerMessage{

    private String message;

    public NotificationSM(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
