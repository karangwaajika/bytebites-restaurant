package com.lab.auth_service.exception;

public class UserExistsException extends AppException{
    public UserExistsException(String message) {
        super(message);
    }
}
