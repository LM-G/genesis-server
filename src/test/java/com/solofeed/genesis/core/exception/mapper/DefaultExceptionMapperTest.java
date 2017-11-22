package com.solofeed.genesis.core.exception.mapper;

import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link DefaultExceptionMapper}
 */
@RunWith(JUnit4.class)
public class DefaultExceptionMapperTest {

    private DefaultExceptionMapper mapper;

    @Before
    public void setUp(){
        mapper = new DefaultExceptionMapper();
    }

    @Test
    public void shouldCreateResponse(){
        // init inputs
        Exception exception = new Exception("foo");

        // execution
        Response errorResponse = mapper.toResponse(exception);

        // assertions
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        ErrorPayload payload = ErrorPayload.class.cast(errorResponse.getEntity());

        assertThat(payload).isNotNull();
        assertThat(payload.getCode()).isEqualTo("E_UNKNOWN");
        assertThat(payload.getMessage()).isEqualTo("Unknown error");
        assertThat(payload.getDetail()).isNull();
        assertThat(payload.getTimestamp()).isCloseTo(System.currentTimeMillis(), Offset.offset(1000L));
    }
}
