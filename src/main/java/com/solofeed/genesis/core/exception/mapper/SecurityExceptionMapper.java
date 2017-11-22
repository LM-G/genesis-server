package com.solofeed.genesis.core.exception.mapper;

import com.solofeed.genesis.core.exception.model.SecurityException;
import com.solofeed.genesis.core.exception.dto.ErrorPayload;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class SecurityExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<SecurityException> {

    @Override
    public Response toResponse(SecurityException e) {
        ErrorPayload payload = new ErrorPayload();

        payload.setCode(e.getCode());
        payload.setMessage(e.getMessage());

        return this.toErrorResponse(e.getStatus(), payload, e, e.getHeaders());
    }
}