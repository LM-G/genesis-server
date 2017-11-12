package com.solofeed.genesis.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class TechnicalException extends APIRuntimeException {
    private final String code;
    private final Map<String, String> headers;

    public TechnicalException(String code, String message, Throwable cause) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, code, message, cause, null);
    }

    public TechnicalException(HttpStatus status, String code, String message, Throwable cause, Map<String, String> headers) {
        super(message, cause);
        this.status = status;
        this.code = code;
        this.headers = headers;
    }
}
