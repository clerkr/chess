package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class InvalidGameException extends Exception{
    public InvalidGameException(String message) {
        super(message);
    }
}
