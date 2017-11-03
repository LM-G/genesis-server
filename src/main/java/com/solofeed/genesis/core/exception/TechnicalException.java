package com.solofeed.genesis.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class TechnicalException extends APIRuntimeException implements IAPIException{
    private String code;

    public TechnicalException(String code, String message, Throwable cause) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, code, message, cause);
    }

    public TechnicalException(HttpStatus status, String code, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.code = code;
    }
}
