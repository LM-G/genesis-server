package com.solofeed.genesis.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class FunctionalException extends APIException{
    private final String code;
    private final Map<String, String> detail;

    public FunctionalException(HttpStatus status, String code, String message) {
        this(status, code, message, null);
    }

    public FunctionalException(HttpStatus status, String code, String message, Map<String, String> detail) {
        super(message);
        this.status = status;
        this.code = code;
        this.detail = detail;
    }
}
