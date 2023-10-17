package com.branas.infrastructure.exceptions.handler;

import com.branas.infrastructure.exceptions.ValidationErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ResourceNotFoundHandler implements ExceptionMapper<ValidationErrorException> {

    @Override
    public Response toResponse(ValidationErrorException e) {
        ErrorResponse errorMessage = new ErrorResponse("validation_error",
                e.getMessage()
        );
        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
