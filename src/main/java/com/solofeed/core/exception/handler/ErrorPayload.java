package com.solofeed.core.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by LM-G on 22/10/2017.
 */
@Data
public class ErrorPayload implements Serializable{
    private String code;
    private String message;
    private Map<String, String> detail;
    private Long timestamp;
    private String stackTrace;
}
