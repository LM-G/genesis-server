package com.solofeed.genesis.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityException extends RuntimeException implements IAPIException{
    private final String code;
    private final HttpStatus status;
    private final Map<String, String> headers;

    public SecurityException(String code, String message) {
        this(HttpStatus.UNAUTHORIZED, code, message);
    }

    public SecurityException(String code, String message, Map<String, String> headers) {
        this(HttpStatus.UNAUTHORIZED, code, message, headers);
    }

    public SecurityException(HttpStatus status, String code, String message) {
        this(status, code, message, new HashMap<>());
    }

    public SecurityException(HttpStatus status, String code, String message, Map<String, String> headers) {
        super(message);
        this.code = code;
        this.status = status;
        this.headers = headers;
    }

    @Override
    public APIExceptionNature getNature() {
        return APIExceptionNature.SECURITY;
    }
}
