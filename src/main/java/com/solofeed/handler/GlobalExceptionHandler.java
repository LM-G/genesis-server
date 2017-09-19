package com.solofeed.handler;

import com.solofeed.exception.UnprocessableEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "yolo ?")
    @ExceptionHandler(UnprocessableEntityException.class)
    public String nullPointerExceptionHandler(final UnprocessableEntityException ex) {
        return ex.getMessage();
    }
}
