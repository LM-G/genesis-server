package com.solofeed.core.exception;

import lombok.Data;


@Data
public class InvalidFormException extends APIException{
    private InvalidFormException(APIExceptionNature nature, String message, Throwable cause){
        super(nature, message, cause);
    }

    public static InvalidFormException with(APIExceptionNature nature, String message, Throwable cause) {
        return new InvalidFormException(nature, message, cause);
    }

    @Override
    public APIExceptionNature getNature() {
        return APIExceptionNature.FUNCTIONAL;
    }
}
