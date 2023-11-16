package exception;

public class UsernameIsExsistsException extends RuntimeException{
    public UsernameIsExsistsException(String message){
        super(message);
    }
}
