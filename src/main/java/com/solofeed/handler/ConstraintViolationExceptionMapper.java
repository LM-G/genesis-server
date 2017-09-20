package com.solofeed.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Provider
public class ConstraintViolationExceptionMapper extends AbstractExceptionMapper implements
        ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<Map<String, ?>> data = new ArrayList();
        Map<String, String> errorMap;

        for (final ConstraintViolation<?> error : e.getConstraintViolations()) {
            errorMap = new HashMap();
            errorMap.put("attribute", error.getPropertyPath().toString());
            errorMap.put("message", error.getMessage());
            data.add(errorMap);
        }

        ResponseEntity re = new ResponseEntity(data, HttpStatus.UNPROCESSABLE_ENTITY);

        return this.errorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), re, e);
    }
}
