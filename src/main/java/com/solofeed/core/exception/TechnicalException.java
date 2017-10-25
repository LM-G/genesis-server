package com.solofeed.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class TechnicalException extends APIException{
    private String code;

    public TechnicalException(HttpStatus status, String code, String message, Throwable cause) {
        super(message, cause);
        this.nature = APIExceptionNature.TECHNICAL;
        this.status = status;
        this.code = code;
    }
}
