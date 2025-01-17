package com.zifan.exception.validation;

public class IllegalUserEmailException extends IllegalArgumentException{
    public IllegalUserEmailException(String message) {
        super(message);
    }

    public IllegalUserEmailException() {
        super("Illegal request: request email not match with authenticated user");
    }
}
