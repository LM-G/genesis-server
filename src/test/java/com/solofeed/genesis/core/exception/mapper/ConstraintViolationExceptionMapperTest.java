package com.solofeed.genesis.core.exception.mapper;

import com.solofeed.genesis.Application;
import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link ConstraintViolationException}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {
    Application.class
})
public class ConstraintViolationExceptionMapperTest {

    private ConstraintViolationExceptionMapper mapper;

    @Inject
    private Validator validator;
    
    @Before
    public void setUp(){
        mapper = new ConstraintViolationExceptionMapper();
    }

    @Test
    public void shouldCreateResponse(){
        // init inputs
        DummyObject dummy = DummyObject.builder().prop_1("").prop_2("foo").prop_3("bar").build();
        Set<ConstraintViolation<DummyObject>> constraints = validator.validate(dummy);
        ConstraintViolationException exception = new ConstraintViolationException(constraints);

        // execution
        Response errorResponse = mapper.toResponse(exception);

        // assertions
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());

        ErrorPayload payload = ErrorPayload.class.cast(errorResponse.getEntity());

        assertThat(payload).isNotNull();
        assertThat(payload.getCode()).isEqualTo("E_FORM_VALIDATION");
        assertThat(payload.getMessage()).isEqualTo("Form validation failed");

        Map<String, String> detail = payload.getDetail();

        assertThat(detail).isNotNull().hasSize(3);

        assertThat(detail.keySet())
            .containsExactlyInAnyOrder("prop_1", "prop_2", "prop_3");

        assertThat(detail.values())
            .containsExactly("ne peut pas être vide", "adresse email mal formée", "la taille doit être comprise entre 5 et 10");

    }

    @Data
    @Builder
    private static class DummyObject {
        @NotEmpty
        private String prop_1;
        @Size(min = 5, max = 10)
        private String prop_2;
        @Email
        private String prop_3;
    }
}
