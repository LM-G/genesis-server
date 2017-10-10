package com.solofeed.core.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Default handler
 */
@Provider
public class DefaultExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        return this.toErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e, e);
    }
}
