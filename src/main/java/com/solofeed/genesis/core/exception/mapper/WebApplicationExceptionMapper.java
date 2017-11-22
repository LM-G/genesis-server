package com.solofeed.genesis.core.exception.mapper;

import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import org.springframework.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Mapper for jersey {@link WebApplicationException}
 */
@Provider
public class WebApplicationExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<WebApplicationException> {
    /** code for all native jersey related errors */
    private static final String DEFAULT_JAXRS_ERROR_CODE = "E_WEB";

    @Override
    public Response toResponse(WebApplicationException e) {
        Response jerseyResponseError = e.getResponse();
        ErrorPayload payload = new ErrorPayload();
        payload.setCode(DEFAULT_JAXRS_ERROR_CODE);
        payload.setMessage(e.getMessage());
        payload.setTimestamp(System.currentTimeMillis());

        return this.toErrorResponse(HttpStatus.valueOf(jerseyResponseError.getStatus()), payload, e);
    }
}
