package dataAccess;

public class ExtantUserException extends Exception{
    public ExtantUserException(String message) {
        super(message);
    }
}
