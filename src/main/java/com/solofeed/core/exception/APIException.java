package com.solofeed.core.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class APIException extends Exception{
    protected APIExceptionNature nature;
    protected HttpStatus status;

    protected APIException(String message){
        super(message);
    }

    protected APIException(String message, Throwable cause){
        super(message, cause);
    }

    public enum APIExceptionNature {
        FUNCTIONAL,
        TECHNICAL
    }
}
