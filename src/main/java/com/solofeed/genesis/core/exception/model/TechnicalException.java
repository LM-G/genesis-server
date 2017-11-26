package com.solofeed.genesis.core.exception.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

/**
 * Describe an exception for technical aspect
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TechnicalException extends APIRuntimeException {
    /**
     * Simple technical exception with http status 500 and no custom headers
     * @param code custom code for front handling
     * @param message debug message
     * @param cause origin of this new exception
     */
    public TechnicalException(String code, String message, Throwable cause) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, code, message, cause, Collections.emptyMap());
    }

    /**
     * Technical exception with custom http status and custom headers
     * @param status http status
     * @param code custom code for front handling
     * @param message debug message
     * @param cause origin of this new exception
     * @param headers custom headers
     */
    public TechnicalException(HttpStatus status, String code, String message, Throwable cause, Map<String, String> headers) {
        super(status, code, message, cause, headers);
    }
}
