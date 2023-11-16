package exception.handler;

import exception.Error;
import exception.InvalidUsernameOrPasswordException;
import exception.UserExistException;
import exception.UsernameIsExsistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerException {

    private ResponseEntity<Object> buildResponseEntity(Error apiError){
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({UsernameIsExsistsException.class})
    public ResponseEntity<Object> handleUsernameIsExsist(UsernameIsExsistsException exception){
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({InvalidUsernameOrPasswordException.class})
    public ResponseEntity<Object> handleInvalidUsernameOrPassword(InvalidUsernameOrPasswordException exception){
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({UserExistException.class})
    public ResponseEntity<Object> handleUserExsist(UserExistException exception){
        return buildResponseEntity(new Error(HttpStatus.NOT_FOUND, exception.getMessage()));
    }

}
