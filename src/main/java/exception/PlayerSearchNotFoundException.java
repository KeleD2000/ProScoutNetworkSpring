package exception;

public class PlayerSearchNotFoundException extends RuntimeException {
    public PlayerSearchNotFoundException (String message) {
        super(message);
    }
}
