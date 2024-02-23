package com.example.szakdoga.exception.handler;

import com.example.szakdoga.exception.*;
import com.example.szakdoga.exception.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerException {
    private ResponseEntity<Object> buildResponseEntity(Error apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({InvalidUsernameOrPasswordException.class})
    public ResponseEntity<Object> handleInvalidUsernameOrPassword(InvalidUsernameOrPasswordException exception){
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({UsernameIsExistsException.class})
    public ResponseEntity<Object> handleUsernameIsExists(UsernameIsExistsException exception){
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({EmailIsExistsException.class})
    public ResponseEntity<Object> handleEmailIsExists(EmailIsExistsException exception){
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException exception){
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({PlayerSearchNotFoundException.class})
    public ResponseEntity<Object> handlePlayerSearchNotFound(PlayerSearchNotFoundException exception){
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({FileUploadException.class})
    public ResponseEntity<Object> handleFileUpload(FileUploadException exception){
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({FileDownloadException.class})
    public ResponseEntity<Object> handleFileDownload(FileDownloadException exception){
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler({PlayerAdNotFoundException.class})
    public ResponseEntity<Object> handlePlayerAdNotFound(PlayerAdNotFoundException exception){
        return buildResponseEntity(new Error(HttpStatus.BAD_REQUEST, exception.getMessage()));
    }

}
