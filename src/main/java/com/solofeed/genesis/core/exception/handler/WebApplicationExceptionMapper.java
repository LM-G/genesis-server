package com.solofeed.genesis.core.exception.handler;

import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import org.springframework.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException e) {
        Response jerseyResponseError = e.getResponse();
        ErrorPayload payload = new ErrorPayload();
        payload.setCode("E_WEB");
        payload.setMessage(e.getMessage());
        payload.setTimestamp(System.currentTimeMillis());

        if(e.getStackTrace() != null && e.getStackTrace().length > 1){
            payload.setStackTrace(e.getStackTrace()[0].toString());
        }

        return this.toErrorResponse(HttpStatus.valueOf(jerseyResponseError.getStatus()), payload, e);
    }
}
