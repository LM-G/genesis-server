package com.solofeed.genesis.core.security.service;

import com.solofeed.genesis.core.exception.model.APIRuntimeException;
import com.solofeed.genesis.core.exception.model.TechnicalException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mindrot.jbcrypt.BCrypt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test {@link PasswordEncoder}
 */
@RunWith(JUnit4.class)
public class PasswordEncoderTest {

    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
        passwordEncoder = new PasswordEncoder();
    }

    @Test
    public void shouldEncode() throws Exception {
        // init
        String password = "foo";

        // execution
        String hashedPassword = passwordEncoder.encode(password);

        // assertions
        assertThat(hashedPassword).isNotNull();
        assertTrue(BCrypt.checkpw(password, hashedPassword));
    }

    @Test
    public void shouldMatch() throws Exception {
        // init
        String hashedPassword = BCrypt.hashpw("foo", BCrypt.gensalt());

        // execution
        boolean samePassword = passwordEncoder.matches("foo", hashedPassword);

        // assertions
        assertTrue(samePassword);
    }

    @Test
    public void shouldNotMatchBecauseDifferentPassword() throws Exception {
        // init
        String hashedPassword = BCrypt.hashpw("foo", BCrypt.gensalt());

        // execution
        boolean samePassword = passwordEncoder.matches("bar", hashedPassword);

        // assertions
        assertFalse(samePassword);
    }

    @Test
    public void shouldNotMatchBecauseInvalidPassword() throws Exception {
        // execution
        assertThatThrownBy(() -> passwordEncoder.matches("bar", "foobarpassword"))
            // assertions
            .isInstanceOf(TechnicalException.class)
            .hasNoCause()
            .hasMessage("Hashed password is invalid")
            .extracting(e -> APIRuntimeException.class.cast(e).getCode())
            .containsExactly("E_CORRUPTED_HASH");
    }

}