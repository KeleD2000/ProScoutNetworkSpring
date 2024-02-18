package com.example.szakdoga.exception;

public class UsernameIsExistsException extends RuntimeException{
    public UsernameIsExistsException(String message){
        super(message);
    }
}
