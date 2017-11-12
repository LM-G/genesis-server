package com.solofeed.genesis.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class APIException extends Exception {
    protected final HttpStatus status;

    APIException(HttpStatus status, String message){
        this(status, message, null);
    }

    protected APIException(HttpStatus status, String message, Throwable cause){
        super(message, cause);
        this.status = status;
    }
}
