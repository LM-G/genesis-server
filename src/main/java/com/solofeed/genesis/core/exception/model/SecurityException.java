package com.solofeed.genesis.core.exception.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Describe an exception for security aspect
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityException extends APIRuntimeException implements IAPIException {

    /**
     * Simple security exception with http status 401 and no custom headers
     * @param code custom code for front handling
     * @param message debug message
     */
    public SecurityException(String code, String message) {
        this(HttpStatus.UNAUTHORIZED, code, message);
    }

    /**
     * Simple security exception with http status 401 and custom headers
     * @param code custom code for front handling
     * @param message debug message
     * @param headers custom headers
     */
    public SecurityException(String code, String message, Map<String, String> headers) {
        this(HttpStatus.UNAUTHORIZED, code, message, headers);
    }

    /**
     * Security exception with custom http status and no custom headers
     * @param status custom headers
     * @param code custom code for front handling
     * @param message debug message
     */
    public SecurityException(HttpStatus status, String code, String message) {
        this(status, code, message, Collections.emptyMap());
    }

    /**
     * Security exception with custom http status and custom headers
     * @param status custom headers
     * @param code custom code for front handling
     * @param message debug message
     * @param headers custom headers
     */
    public SecurityException(HttpStatus status, String code, String message, Map<String, String> headers) {
        super(status, code, message, headers);
    }
}
