package com.solofeed.core.handler;


import lombok.extern.log4j.Log4j2;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Log4j2
public abstract class AbstractExceptionMapper {

    protected Response toErrorResponse(int status, ErrorBody body) {
        return create(status, body);
    }

    protected Response toErrorResponse(int status, ErrorBody body, Throwable t) {
        LOGGER.trace(t);

        return create(status, body);
    }

    private Response create(int status, ErrorBody body) {
        return Response.status(status).entity(body).header("content-type", MediaType.APPLICATION_JSON).build();
    }
}
