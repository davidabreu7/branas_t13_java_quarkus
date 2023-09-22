package com.branas.infrastructure.Exceptions;

public class InvalidValidationException extends RuntimeException{
    public InvalidValidationException(String message) {
        super(message);
    }
}
