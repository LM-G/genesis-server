package com.solofeed.core.handler;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ConstraintViolationExceptionMapper extends AbstractExceptionMapper implements
        ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        Map<String, String> detail = new HashMap();

        for (final ConstraintViolation<?> error : e.getConstraintViolations()) {
            detail.put(((PathImpl) error.getPropertyPath()).getLeafNode().getName(), error.getMessage());
        }

        return this.toErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), detail);
    }
}
