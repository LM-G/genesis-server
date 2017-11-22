package com.solofeed.genesis.core.exception.mapper;

import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Default mapper for unmanaged {@link Exception}
 */
@Provider
public class DefaultExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<Exception> {
    /** Default error code */
    private static final String DEFAULT_CODE = "E_UNKNOWN";
    /** Default debug message */
    private static final String DEFAULT_MESSAGE = "Unknown error";

    @Override
    public Response toResponse(Exception e) {
        ErrorPayload payload = new ErrorPayload();
        payload.setCode(DEFAULT_CODE);
        payload.setMessage(DEFAULT_MESSAGE);
        payload.setTimestamp(System.currentTimeMillis());
        return this.toErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, payload, e);
    }
}
