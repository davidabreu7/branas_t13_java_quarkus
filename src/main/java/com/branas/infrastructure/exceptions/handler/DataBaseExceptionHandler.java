package com.branas.infrastructure.exceptions.handler;

import com.branas.infrastructure.exceptions.DataBaseException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;


@Provider
@Slf4j
public class DataBaseExceptionHandler implements ExceptionMapper<DataBaseException> {

    @Override
    public Response toResponse(DataBaseException e) {
        log.error("DataBaseException: {}", e.getMessage());
        ErrorResponse errorMessage = new ErrorResponse(UUID.randomUUID().toString(),
                e.getMessage()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
    }
}
