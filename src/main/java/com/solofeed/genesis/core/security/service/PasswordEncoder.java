package com.solofeed.genesis.core.security.service;

import com.solofeed.genesis.core.security.api.exception.AuthError;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * Hash passwords and match plain one and hashed one
 */
@Component
public class PasswordEncoder {
    /**
     * Hashes a password using BCrypt.
     *
     * @param plainTextPassword password to encrypt
     * @return
     */
    public String encode(String plainTextPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    /**
     * Checks a password against a stored hash using BCrypt.
     *
     * @param plainTextPassword password to check
     * @param hashedPassword hashed password
     * @return true if the password is correct, else false
     */
    public boolean matches(String plainTextPassword, String hashedPassword) {

        if (!StringUtils.startsWith(hashedPassword, "$2a$")) {
            throw AuthError.ofCorruptedPassword();
        }

        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
