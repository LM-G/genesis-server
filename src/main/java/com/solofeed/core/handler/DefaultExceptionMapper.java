package com.solofeed.core.handler;

import org.apache.commons.lang3.StringUtils;
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
        ErrorResponseContent body = ErrorResponseContent.builder()
                .code("E_UNKNOWN")
                .message("Unknown error")
                .timestamp(System.currentTimeMillis())
                .stackTrace(StringUtils.join(e.getStackTrace(), '\n'))
                .build();

        return this.toErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), body, e);
    }
}
