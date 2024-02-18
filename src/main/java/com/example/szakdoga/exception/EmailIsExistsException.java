package com.example.szakdoga.exception;

public class EmailIsExistsException extends RuntimeException{
    public EmailIsExistsException(String message){
        super(message);
    }
}
