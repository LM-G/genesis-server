package com.solofeed.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
@Provider
public class BaseExceptionMapper extends AbstractExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        ResponseEntity re = new ResponseEntity(null, null);
        return this.errorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), re, e);
    }
}
