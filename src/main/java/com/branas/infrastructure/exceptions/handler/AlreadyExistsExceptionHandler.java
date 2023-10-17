package com.branas.infrastructure.exceptions.handler;

import com.branas.infrastructure.exceptions.AlreadyExistsException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;


@Provider
@Slf4j
public class AlreadyExistsExceptionHandler implements ExceptionMapper<AlreadyExistsException> {
    @Override
    public Response toResponse(AlreadyExistsException e) {
        log.error("AlreadyExistsException: {}", e.getMessage());
        ErrorResponse errorMessage = new ErrorResponse(UUID.randomUUID().toString(),
                e.getMessage()
        );
        return Response.status(Response.Status.CONFLICT)
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
