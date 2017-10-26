package com.solofeed.core.exception.handler;

import org.springframework.http.HttpStatus;

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
        ErrorPayload payload = new ErrorPayload();
        payload.setCode("E_UNKNOWN");
        payload.setMessage("Unknown error");
        payload.setTimestamp(System.currentTimeMillis());

        if(e.getStackTrace() != null && e.getStackTrace().length > 1){
            payload.setStackTrace(e.getStackTrace()[0].toString());
        }

        return this.toErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, payload, e);
    }
}
