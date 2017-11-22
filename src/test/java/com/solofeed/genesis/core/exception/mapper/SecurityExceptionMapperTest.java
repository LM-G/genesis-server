package com.solofeed.genesis.core.exception.mapper;

import com.google.common.collect.ImmutableMap;
import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import com.solofeed.genesis.core.exception.model.SecurityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link SecurityExceptionMapper}
 */
@RunWith(JUnit4.class)
public class SecurityExceptionMapperTest {
    private static final HttpStatus HTTP_STATUS = HttpStatus.I_AM_A_TEAPOT;
    private static final String CODE = "foo";
    private static final String MESSAGE = "bar";

    private SecurityExceptionMapper mapper;

    @Before
    public void setUp(){
        mapper = new SecurityExceptionMapper();
    }

    @Test
    public void shouldCreateResponse(){
        // init inputs
        SecurityException exception = new SecurityException(HTTP_STATUS, CODE, MESSAGE);

        // execution
        Response errorResponse = mapper.toResponse(exception);

        // assertions
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HTTP_STATUS.value());

        checkPayload(errorResponse, CODE, MESSAGE);
    }

    @Test
    public void shouldCreateResponseWithDefaultStatus(){
        // init inputs
        SecurityException exception = new SecurityException(CODE, MESSAGE);

        // execution
        Response errorResponse = mapper.toResponse(exception);

        // assertions
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

        checkPayload(errorResponse, CODE, MESSAGE);
    }

    @Test
    public void shouldCreateResponseWithHeaders(){
        // init inputs
        SecurityException exception = new SecurityException(HTTP_STATUS, CODE, MESSAGE, ImmutableMap.of("bar", "foo"));

        // execution
        Response errorResponse = mapper.toResponse(exception);

        // assertions
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HTTP_STATUS.value());
        assertThat(errorResponse.getHeaders()).hasSize(2);
        assertThat(errorResponse.getHeaders().get("bar").get(0)).isEqualTo("foo");

        checkPayload(errorResponse, CODE, MESSAGE);
    }

    private void checkPayload(Response errorResponse, String code, String message){
        ErrorPayload payload = ErrorPayload.class.cast(errorResponse.getEntity());

        assertThat(payload).isNotNull();
        assertThat(payload.getCode()).isEqualTo(code);
        assertThat(payload.getMessage()).isEqualTo(message);
    }
}
