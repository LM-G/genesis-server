package com.solofeed.core.handler;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Log4j2
public abstract class AbstractExceptionMapper {

    protected Response toErrorResponse(HttpStatus status, ErrorResponseContent body) {
        return create(status, body);
    }

    protected Response toErrorResponse(HttpStatus status, ErrorResponseContent body, Throwable t) {
        LOGGER.trace(t);
        return create(status, body);
    }

    private Response create(HttpStatus status, ErrorResponseContent body) {
        return Response.status(status.value()).entity(body).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).build();
    }
}
