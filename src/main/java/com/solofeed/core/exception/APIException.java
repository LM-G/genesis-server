package com.solofeed.core.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public abstract class APIException extends Exception{
    protected APIExceptionNature nature;
    protected HttpStatus status;

    protected APIException(APIExceptionNature nature, String message, Throwable cause){
        super(message, cause);
        this.nature = nature;
    }

    public static enum APIExceptionNature {
        FUNCTIONAL,
        TECHNICAL
    }
}
