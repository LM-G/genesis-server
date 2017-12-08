package com.solofeed.genesis.user.exception;

import com.google.common.collect.ImmutableMap;
import com.solofeed.genesis.core.exception.model.FunctionalException;
import com.solofeed.utilities.UtilityClassChecker;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test {@link UserActionError}
 */
public class UserActionErrorTest {

    @Test
    public void shouldReturnRegistrationFailedError() throws Exception {
        // execution
        FunctionalException exception = UserActionError.ofRegistrationFailed(true, false);

        Map<String, String> detail = ImmutableMap.of("email","false","name","true");

        // assertions
        assertThat(exception).isNotNull();
        assertThat(exception).hasMessage("User registration failed");
        assertThat(exception.getStatus()).isEqualByComparingTo(HttpStatus.CONFLICT);
        assertThat(exception.getCode()).isEqualTo("E_USER_REGISTRATION_FAILED");
        assertThat(exception.getDetail()).isEqualTo(detail);
    }

    @Test
    public void shouldReturnNotFoundError() throws Exception {
        // execution
        FunctionalException exception = UserActionError.ofNotFound("foo");

        // assertions
        assertThat(exception).isNotNull();
        assertThat(exception).hasMessage("foo");
        assertThat(exception.getStatus()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getCode()).isEqualTo("E_USER_NOT_FOUND");
        assertThat(exception.getDetail()).isNull();
    }

    @Test
    public void shouldBeUtilityClass() throws Exception {
        UtilityClassChecker.check(UserActionError.class);
    }

}