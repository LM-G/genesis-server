package com.solofeed.genesis.core.exception.mapper;

import com.google.common.collect.ImmutableMap;
import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import com.solofeed.genesis.core.exception.model.FunctionalException;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link FunctionalExceptionMapper}
 */
@RunWith(JUnit4.class)
public class FunctionalExceptionMapperTest {
    private static final HttpStatus HTTP_STATUS = HttpStatus.I_AM_A_TEAPOT;
    private static final String CODE = "foo";
    private static final String MESSAGE = "bar";
    private static final Map<String, String> DETAIL = ImmutableMap.of("foo", "bar");

    private FunctionalExceptionMapper mapper;

    @Before
    public void setUp(){
        mapper = new FunctionalExceptionMapper();
    }

    @Test
    public void shouldCreateResponse(){
        // init inputs
        FunctionalException exception = new FunctionalException(HTTP_STATUS, CODE, MESSAGE);

        // execution
        Response errorResponse = mapper.toResponse(exception);

        // assertions
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HTTP_STATUS.value());

        checkPayload(errorResponse, CODE, MESSAGE, null);
    }

    @Test
    public void shouldCreateResponseWithDetail(){
        // init inputs
        FunctionalException exception = new FunctionalException(HTTP_STATUS, CODE, MESSAGE, DETAIL);

        // execution
        Response errorResponse = mapper.toResponse(exception);

        // assertions
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HTTP_STATUS.value());

        checkPayload(errorResponse, CODE, MESSAGE, DETAIL);
    }

    @Test
    public void shouldCreateResponseWithHeaders(){
        // init inputs
        FunctionalException exception = new FunctionalException(HTTP_STATUS, CODE, MESSAGE, null, ImmutableMap.of("bar", "foo"));

        // execution
        Response errorResponse = mapper.toResponse(exception);

        // assertions
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HTTP_STATUS.value());
        assertThat(errorResponse.getHeaders()).hasSize(2);
        assertThat(errorResponse.getHeaders().get("bar").get(0)).isEqualTo("foo");

        checkPayload(errorResponse, CODE, MESSAGE, null);
    }

    private void checkPayload(Response errorResponse, String code, String message, Map<String, String> detail){
        ErrorPayload payload = ErrorPayload.class.cast(errorResponse.getEntity());

        assertThat(payload).isNotNull();
        assertThat(payload.getCode()).isEqualTo(code);
        assertThat(payload.getMessage()).isEqualTo(message);
        if(!CollectionUtils.isEmpty(detail)){
            assertThat(payload.getDetail()).isEqualToComparingFieldByField(detail);
        }
    }
}
