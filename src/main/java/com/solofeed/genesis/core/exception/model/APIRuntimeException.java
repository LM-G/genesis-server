package com.solofeed.genesis.core.exception.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class APIRuntimeException extends RuntimeException implements IAPIException {
    protected final String code;
    protected final HttpStatus status;
    protected final transient Map<String, String> headers;

    APIRuntimeException(HttpStatus status, String code, String message){
        this(status, code, message, null, Collections.emptyMap());
    }

    APIRuntimeException(HttpStatus status, String code, String message, Map<String, String> headers){
        this(status, code, message, null, headers);
    }

    APIRuntimeException(HttpStatus status, String code, String message, Throwable cause, Map<String, String> headers){
        super(message, cause);
        this.status = status;
        this.code = code;
        this.headers = headers;
    }
}
