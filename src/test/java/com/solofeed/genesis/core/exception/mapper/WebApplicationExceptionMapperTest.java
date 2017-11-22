package com.solofeed.genesis.core.exception.mapper;

import com.solofeed.genesis.core.exception.dto.ErrorPayload;
import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpStatus;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link WebApplicationExceptionMapper}
 */
@RunWith(JUnit4.class)
public class WebApplicationExceptionMapperTest {

    private WebApplicationExceptionMapper mapper;
    
    @Before
    public void setUp(){
        mapper = new WebApplicationExceptionMapper();
    }

    @Test
    public void shouldCreateResponse(){
        // init inputs
        NotFoundException exception = new NotFoundException("foo");

        // execution
        Response errorResponse = mapper.toResponse(exception);

        // assertions
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getStatus()).isEqualTo(exception.getResponse().getStatus());

        ErrorPayload payload = ErrorPayload.class.cast(errorResponse.getEntity());

        assertThat(payload).isNotNull();
        assertThat(payload.getCode()).isEqualTo("E_WEB");
        assertThat(payload.getMessage()).isEqualTo(exception.getMessage());
        assertThat(payload.getDetail()).isNull();
        assertThat(payload.getTimestamp()).isCloseTo(System.currentTimeMillis(), Offset.offset(1000L));
    }
}
