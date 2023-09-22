package com.branas.infrastructure.exceptions.handler;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.sql.SQLException;

@Provider
public class SQLExceptionHandler implements ExceptionMapper<SQLException> {
    @Override
    public Response toResponse(SQLException throwables) {
        ErrorResponse errorMessage = new ErrorResponse("sql_error",
                throwables.getMessage()
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
    }
}
