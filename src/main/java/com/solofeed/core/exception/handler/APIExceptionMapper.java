package com.solofeed.core.exception.handler;

import com.solofeed.core.exception.APIException;
import com.solofeed.core.exception.FunctionalException;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;


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