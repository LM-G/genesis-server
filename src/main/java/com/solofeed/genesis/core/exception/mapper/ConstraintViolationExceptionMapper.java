package com.solofeed.genesis.core.exception.mapper;

import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for {@link ConstraintViolation}
 */
@Provider
public class ConstraintViolationExceptionMapper extends AbstractExceptionMapper implements
        ExceptionMapper<ConstraintViolationException> {

    /** Debug message for failed form validation */
    private static final String VALIDATION_FAILED_MESSAGE = "Form validation failed";

    /** Error code for failed form validation */
    private static final String VALIDATION_FAILED_CODE = "E_FORM_VALIDATION";

    @Override
    public Response toResponse(ConstraintViolationException e) {
        Map<String, String> detail = new HashMap<>();

        ErrorPayload payload = new ErrorPayload();
        payload.setMessage(VALIDATION_FAILED_MESSAGE);
        payload.setCode(VALIDATION_FAILED_CODE);
        payload.setDetail(detail);

        for (final ConstraintViolation<?> error : e.getConstraintViolations()) {
            detail.put(((PathImpl) error.getPropertyPath()).getLeafNode().getName(), error.getMessage());
        }

        return this.toErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, payload);
    }
}
