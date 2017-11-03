package com.solofeed.genesis.core.exception.dto;

import lombok.Data;

import java.util.Map;

/**
 * Error payload with fields discribing the problem
 */
@Data
public class ErrorPayload {
    /** Error unique code */
    private String code;
    /** Debug message */
    private String message;
    /** Detail specific to some errors */
    private Map<String, String> detail;
    /** Date of the error */
    private Long timestamp;
    /** Debug stacktrace */
    private String stackTrace;
}
