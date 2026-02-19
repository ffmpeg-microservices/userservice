package com.mediaalterations.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException{
    private String message;
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
