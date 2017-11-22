package com.solofeed.genesis.core.exception;

import com.solofeed.genesis.core.exception.model.FunctionalException;
import com.solofeed.utilities.UtilityClassChecker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link GenericError}
 */
@RunWith(JUnit4.class)
public class GenericErrorTest {
    @Test
    public void shouldReturnNotFoundError() {
        String code = "foo";
        String message = "bar";
        FunctionalException exception = GenericError.ofNotFound(code, message);

        assertThat(exception).isNotNull();
        assertThat(exception)
            .hasNoCause()
            .hasMessage("bar");
        assertThat(exception.getCode()).isEqualTo(code);
        assertThat(exception.getDetail()).isNull();
        assertThat(exception.getHeaders()).isEmpty();
        assertThat(exception.getStatus()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldBeUtilityClass() throws Exception {
        UtilityClassChecker.check(GenericError.class);
    }
}
