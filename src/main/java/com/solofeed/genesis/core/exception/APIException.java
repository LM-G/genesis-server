package com.solofeed.genesis.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class APIException extends Exception implements IAPIException{
    protected HttpStatus status;

    protected APIException(String message){
        super(message);
    }

    protected APIException(String message, Throwable cause){
        super(message, cause);
    }

    @Override
    public APIExceptionNature getNature() {
        return APIExceptionNature.FUNCTIONAL;
    }
}
