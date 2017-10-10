package com.solofeed.core.handler;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;

@Log4j2
public abstract class AbstractExceptionMapper {

    protected Response toErrorResponse(int status, Object detail) {
        return create(status, detail);
    }

    protected Response toErrorResponse(int status, Object detail, Throwable t) {
        LOGGER.trace(detail, t);

        return create(status, detail);
    }

    private Response create(int status, Object detail) {
        return Response.status(status).entity(detail).build();
    }
}
