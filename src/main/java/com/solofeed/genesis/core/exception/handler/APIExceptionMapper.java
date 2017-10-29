package com.solofeed.genesis.core.exception.handler;

import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.core.exception.FunctionalException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class APIExceptionMapper extends AbstractExceptionMapper implements
        ExceptionMapper<APIException> {

    @Override
    public Response toResponse(APIException e) {
        ErrorPayload payload = new ErrorPayload();

        if(FunctionalException.class.isAssignableFrom(e.getClass())) {
            FunctionalException fe = FunctionalException.class.cast(e);
            payload.setCode(fe.getCode());
            payload.setMessage(fe.getMessage());
            payload.setDetail(fe.getDetail());
        }

        return this.toErrorResponse(e.getStatus(), payload, e);
    }
}