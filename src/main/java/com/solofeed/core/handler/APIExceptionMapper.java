package com.solofeed.core.handler;

import com.solofeed.core.exception.APIException;
import com.solofeed.core.exception.FunctionalException;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class APIExceptionMapper extends AbstractExceptionMapper implements
        ExceptionMapper<APIException> {

    @Override
    public Response toResponse(APIException e) {
        ErrorResponseContent.Builder errorContentBuilder = ErrorResponseContent.builder();

        if(FunctionalException.class.isAssignableFrom(e.getClass())) {
            FunctionalException fe = FunctionalException.class.cast(e);
            errorContentBuilder.code(fe.getCode()).message(fe.getMessage()).detail(fe.getDetail());
        }

        return this.toErrorResponse(e.getStatus(), errorContentBuilder.build(), e);
    }
}