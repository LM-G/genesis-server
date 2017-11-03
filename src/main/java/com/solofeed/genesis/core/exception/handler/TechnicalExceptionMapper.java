package com.solofeed.genesis.core.exception.handler;

import com.solofeed.genesis.core.exception.TechnicalException;
import com.solofeed.genesis.core.exception.dto.ErrorPayload;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class TechnicalExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<TechnicalException> {

    @Override
    public Response toResponse(TechnicalException e) {
        ErrorPayload payload = new ErrorPayload();

        payload.setCode(e.getCode());
        payload.setMessage(e.getMessage());

        return this.toErrorResponse(e.getStatus(), payload, e);
    }
}