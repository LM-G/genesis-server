package com.solofeed.genesis.core.exception.handler;

import com.solofeed.genesis.core.exception.FunctionalException;
import com.solofeed.genesis.core.exception.dto.ErrorPayload;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class FunctionalExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<FunctionalException> {

    @Override
    public Response toResponse(FunctionalException e) {
        ErrorPayload payload = new ErrorPayload();

        payload.setCode(e.getCode());
        payload.setMessage(e.getMessage());
        payload.setDetail(e.getDetail());

        return this.toErrorResponse(e.getStatus(), payload, e);
    }
}