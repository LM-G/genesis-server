package com.solofeed.genesis.core.exception.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Describe an exception for functional aspect
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FunctionalException extends APIException {
    private final Map<String, String> detail;

    public FunctionalException(HttpStatus status, String code, String message) {
        this(status, code, message, null);
    }

    public FunctionalException(HttpStatus status, String code, String message, Map<String, String> detail) {
        this(status, code, message, detail, Collections.emptyMap());
    }

    public FunctionalException(HttpStatus status, String code, String message, Map<String, String> detail, Map<String, String> headers) {
        super(status, code, message, headers);
        this.detail = detail;
    }
}
