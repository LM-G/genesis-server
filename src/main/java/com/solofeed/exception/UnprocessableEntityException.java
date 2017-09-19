package com.solofeed.exception;

import javax.ws.rs.ClientErrorException;

public class UnprocessableEntityException extends ClientErrorException {
    private static final int STATUS = 422;

    public UnprocessableEntityException(){
        super(STATUS);
    }
}
