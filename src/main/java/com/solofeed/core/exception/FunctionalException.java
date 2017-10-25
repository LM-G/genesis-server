package com.solofeed.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class FunctionalException extends APIException{
    private String code;
    private Map<String, String> detail;

    public FunctionalException(HttpStatus status, String code, String message) {
        this(status, code, message, null);
    }

    public FunctionalException(HttpStatus status, String code, String message, Map<String, String> detail) {
        super(message);
        this.nature = APIExceptionNature.FUNCTIONAL;
        this.status = status;
        this.code = code;
        this.detail = detail;
    }
}
