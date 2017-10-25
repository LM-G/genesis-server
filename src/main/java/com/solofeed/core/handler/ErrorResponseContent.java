package com.solofeed.core.handler;

import com.solofeed.core.CoreConstants;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by LM-G on 22/10/2017.
 */
@Data
@Builder(builderClassName = CoreConstants.BUILDER_NAME)
public class ErrorResponseContent implements Serializable{
    private String message;
    private Long timestamp;
    private Object detail;
    private String code;
    private String stackTrace;
}