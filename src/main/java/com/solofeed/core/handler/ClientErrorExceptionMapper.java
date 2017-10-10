package com.solofeed.core.handler;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ClientErrorExceptionMapper extends AbstractExceptionMapper implements
        ExceptionMapper<ClientErrorException> {

    @Override
    public Response toResponse(ClientErrorException e) {
        return e.getResponse();
    }
}
