package com.branas.infrastructure.exceptions;

import lombok.Getter;

@Getter
public class ValidationErrorException extends RuntimeException{
    public ValidationErrorException(String message) {
        super(message);
    }
    public ValidationErrorException(String message, Throwable cause) {
        super(message, cause);
    }
    public ValidationErrorException(Throwable cause) {
        super(cause);
    }
    public ValidationErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
