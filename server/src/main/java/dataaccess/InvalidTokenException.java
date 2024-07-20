package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class InvalidTokenException extends Exception{
    public InvalidTokenException(String message) {
        super(message);
    }
}
