package com.solofeed.genesis.core.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class APIRuntimeException extends RuntimeException implements IAPIException{
    protected HttpStatus status;

    protected APIRuntimeException(String message){
        super(message);
    }

    protected APIRuntimeException(String message, Throwable cause){
        super(message, cause);
    }

    @Override
    public APIExceptionNature getNature() {
        return APIExceptionNature.TECHNICAL;
    }
}
