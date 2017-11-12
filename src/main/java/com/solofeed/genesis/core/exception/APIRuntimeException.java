package com.solofeed.genesis.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class APIRuntimeException extends RuntimeException {
    protected final HttpStatus status;

    protected APIRuntimeException(HttpStatus status, String message){
        this(status, message, null);
    }

    protected APIRuntimeException(HttpStatus status, String message, Throwable cause){
        super(message, cause);
        this.status = status;
    }
}
