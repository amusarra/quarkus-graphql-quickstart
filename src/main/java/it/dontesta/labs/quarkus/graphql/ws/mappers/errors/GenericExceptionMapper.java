/*
 * Copyright (c) 2025 Antonio Musarra's Blog.
 * SPDX-License-Identifier: MIT
 */
package it.dontesta.labs.quarkus.graphql.ws.mappers.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import io.quarkus.logging.Log;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * This exception mapper catch all throwable and is the last in the chain
 * because is the most generic one. Remember that only one mapper can be
 * triggered in each exception managing process.
 */
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private final ObjectMapper objectMapper;

    @Inject
    public GenericExceptionMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Response toResponse(Throwable exception) {
        Log.error("An error occurred", exception);

        int code = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        if (exception instanceof WebApplicationException webAppException) {
            code = webAppException.getResponse().getStatus();
        }

        ObjectNode exceptionJson = objectMapper.createObjectNode();
        exceptionJson.put("code", code);
        exceptionJson.put("exceptionType", exception.getClass().getName());

        if (exception.getMessage() != null) {
            exceptionJson.put("error", exception.getMessage());
        }

        return Response.status(code).entity(exceptionJson).build();
    }
}
