package com.solofeed.core.handler;

import com.solofeed.core.exception.APIException;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class APIExceptionMapper extends AbstractExceptionMapper implements
        ExceptionMapper<APIException> {

    @Override
    public Response toResponse(APIException e) {
        ErrorBody.ErrorBodyBuilder bodyBuilder = ErrorBody.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .timestamp(System.currentTimeMillis());

        if(e.getStackTrace() != null && e.getStackTrace().length > 0) {
            bodyBuilder.stackTrace(StringUtils.join(e.getStackTrace(), '\n'));
        }

        return this.toErrorResponse(e.getStatus().value(), bodyBuilder.build(), e);
    }
}