package com.branas.infrastructure.exceptions.handler;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ErrorResponse {

    String errorId;
    String message;

    public ErrorResponse(String errorId, String message) {
        this.errorId = errorId;
        this.message = message;
    }
}
