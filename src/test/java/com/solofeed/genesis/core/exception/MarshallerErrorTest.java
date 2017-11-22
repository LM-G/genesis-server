package com.solofeed.genesis.core.exception;

import com.google.gson.JsonParseException;
import com.solofeed.genesis.core.exception.model.TechnicalException;
import com.solofeed.utilities.UtilityClassChecker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test {@link MarshallerError}
 */
@RunWith(JUnit4.class)
public class MarshallerErrorTest {
    @Test
    public void shouldCreateTechnicalExceptionOfMarshalling(){
        // init inputs
        JsonParseException fooException = new JsonParseException("foo");

        // execution
        TechnicalException exception = MarshallerError.ofMarshalling(fooException);

        // assertions
        assertThat(exception).isNotNull();
        assertThat(exception)
            .hasCause(fooException)
            .hasMessage("Erreur lors de la sérialisation");
        assertThat(exception.getCode()).isEqualTo(MarshallerError.E_COULDNOT_MARSHAL);
        assertThat(exception.getHeaders()).isEmpty();
        assertThat(exception.getStatus()).isEqualByComparingTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldCreateTechnicalExceptionOfUnmarshalling(){
        // init inputs
        JsonParseException fooException = new JsonParseException("foo");

        // execution
        TechnicalException exception = MarshallerError.ofUnmarshalling(fooException);

        // assertions
        assertThat(exception).isNotNull();
        assertThat(exception)
            .hasCause(fooException)
            .hasMessage("Erreur lors de la désérialisation");
        assertThat(exception.getCode()).isEqualTo(MarshallerError.E_COULDNOT_UNMARSHAL);
        assertThat(exception.getHeaders()).isEmpty();
        assertThat(exception.getStatus()).isEqualByComparingTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldBeUtilityClass() throws Exception {
        UtilityClassChecker.check(MarshallerError.class);
    }

}
