package com.solofeed.core.exception.handler;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Log4j2
@Singleton
public abstract class AbstractExceptionMapper {

    protected Response toErrorResponse(HttpStatus status, ErrorPayload payload) {
        return create(status, payload);
    }

    protected Response toErrorResponse(HttpStatus status, ErrorPayload payload, Throwable t) {
        LOGGER.trace(t);
        return create(status, payload);
    }

    /**
     * Returns a response error to client
     * @param status http status
     * @param payload error content
     * @return http response
     */
    private Response create(HttpStatus status, ErrorPayload payload) {
        return Response.status(status.value())
                .type(MediaType.APPLICATION_JSON)
                .entity(payload)
                .build();
    }
}
