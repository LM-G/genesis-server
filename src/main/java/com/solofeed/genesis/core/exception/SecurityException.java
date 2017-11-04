package com.solofeed.genesis.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityException extends AuthenticationException implements IAPIException{
    private final String code;
    private final HttpStatus status;

    public SecurityException(String code, String message) {
        this(HttpStatus.UNAUTHORIZED, code, message);
    }

    public SecurityException(HttpStatus status, String code, String message) {
        super(message);
        this.code = code;
        this.status = status;
    }

    @Override
    public APIExceptionNature getNature() {
        return APIExceptionNature.SECURITY;
    }
}
