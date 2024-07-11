package chess;

/**
 * Indicates that a King was not detected on the board... ideally should not occur
 */
public class NoKingException extends Exception {

    public NoKingException() {}

    public NoKingException(String message) {
        super(message);
    }
}
