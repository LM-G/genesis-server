package com.solofeed.core.exception;

import org.springframework.http.HttpStatus;


public class FunctionalException extends APIException{
    private FunctionalException(HttpStatus status, String code, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
    }

    public static FunctionalException with(HttpStatus status, String code, String message, Throwable cause) {
        return new FunctionalException(status, code, message, cause);
    }
}
