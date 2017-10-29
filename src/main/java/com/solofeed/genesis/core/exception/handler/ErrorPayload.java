package com.solofeed.genesis.core.exception.handler;

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
