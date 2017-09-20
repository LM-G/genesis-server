package com.solofeed.handler;


import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;

public abstract class AbstractExceptionMapper {
    private final Logger LOGGER = Logger.getLogger(getClass());

    protected Response errorResponse(int status, ResponseEntity responseEntity) {
        return customizeResponse(status, responseEntity);
    }

    protected Response errorResponse(int status, ResponseEntity responseEntity, Throwable t) {
        if(LOGGER.isTraceEnabled()){
            LOGGER.trace(t);
        }

        return customizeResponse(status, responseEntity);
    }

    private Response customizeResponse(int status, ResponseEntity responseEntity) {
        return Response.status(status).entity(responseEntity).build();
    }
}
