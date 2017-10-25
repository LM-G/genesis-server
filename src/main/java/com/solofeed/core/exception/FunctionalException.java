package com.solofeed.core.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FunctionalException extends APIException{
    private String code;
    private Object detail;

    public FunctionalException(HttpStatus status, String code, String message, Object detail) {
        super(message);
        this.nature = APIExceptionNature.FUNCTIONAL;
        this.status = status;
        this.code = code;
        this.detail = detail;
    }
}
