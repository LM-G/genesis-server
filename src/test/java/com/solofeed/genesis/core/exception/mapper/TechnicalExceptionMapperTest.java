package com.solofeed.genesis.core.exception.mapper;

import com.google.common.collect.ImmutableMap;
import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import com.solofeed.genesis.core.exception.model.SecurityException;
import com.solofeed.genesis.core.exception.model.TechnicalException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link TechnicalExceptionMapper}
 */
@RunWith(JUnit4.class)
public class TechnicalExceptionMapperTest {
    private static final HttpStatus HTTP_STATUS = HttpStatus.I_AM_A_TEAPOT;
    private static final String CODE = "foo";
    private static final String MESSAGE = "bar";

    private TechnicalExceptionMapper mapper;

    @Before
    public void setUp(){
        mapper = new TechnicalExceptionMapper();
    }

    @Test
    public void shouldCreateResponse(){
        // init inputs
        TechnicalException exception = new TechnicalException(HTTP_STATUS, CODE, MESSAGE, new RuntimeException("foo"), Collections.emptyMap());

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
        TechnicalException exception = new TechnicalException(CODE, MESSAGE, new RuntimeException("foo"));

        // execution
        Response errorResponse = mapper.toResponse(exception);

        // assertions
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        checkPayload(errorResponse, CODE, MESSAGE);
    }

    @Test
    public void shouldCreateResponseWithHeaders(){
        // init inputs
        TechnicalException exception = new TechnicalException(HTTP_STATUS, CODE, MESSAGE, new RuntimeException("foo"), ImmutableMap.of("bar", "foo"));

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
