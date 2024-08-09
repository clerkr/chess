package websocket.messages;

public class NotificationSM extends ServerMessage{
    public NotificationSM(ServerMessageType type, String message) {
        super(type, message);
    }
}
