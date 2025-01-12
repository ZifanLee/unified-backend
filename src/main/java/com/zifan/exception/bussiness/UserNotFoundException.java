package com.zifan.exception.bussiness;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String fieldName, String fieldValue) {
        super("User not found with " + fieldName + ": " + fieldValue);
    }
}

