package com.solofeed.genesis.core.security.api.exception;

import com.solofeed.genesis.core.exception.model.APIException;
import com.solofeed.genesis.core.exception.model.FunctionalException;
import com.solofeed.genesis.core.exception.model.SecurityException;
import com.solofeed.genesis.core.exception.model.TechnicalException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Exception builder for authentication failures
 */
@UtilityClass
public class AuthError {
    private static final String WRONG_CREDENTIALS = "E_WRONG_CREDENTIALS";
    private static final String TOKEN_CREATION = "E_TOKEN_CREATION";
    private static final String MISSING_TOKEN = "E_MISSING_TOKEN";
    private static final String EXPIRED_TOKEN = "E_EXPIRED_TOKEN";
    private static final String INVALID_TOKEN = "E_INVALID_TOKEN";
    private static final String INSUFFICIENT_PERMISSION = "E_INSUFFICIENT_PERMISSION";
    private static final String OUT_OF_DATE_TOKEN = "E_OUT_OF_DATE_TOKEN";
    private static final String CORRUPTED_HASH = "E_CORRUPTED_HASH";

    /**
     * User supplied the wrong credentials
     * @return functional exception of wrong credentials
     */
    public static SecurityException ofWrongCredentials(){
        return new SecurityException(WRONG_CREDENTIALS, "Authentication failed, wrong credentials");
    }

    /**
     * Server couldn't create JWT
     * @param cause root cause
     * @return technical exception of token creation failure
     */
    public static TechnicalException ofTokenCreationFailure(Throwable cause){
        return new TechnicalException(TOKEN_CREATION, "Token creation failed", cause);
    }

    /**
     * Client doesn't supplied JWT in his request
     * @param headers custom headers to send alongside the error payload
     * @return security exception of missing JWT
     */
    public static SecurityException ofMissingJWT(Map<String, String> headers){
        return new SecurityException(MISSING_TOKEN, "JWT is missing", headers);
    }

    /**
     * JWT send by client is expired and no longer valid
     * @param message debug message
     * @param headers custom headers to send alongside the error payload
     * @return security exception of expired JWT
     */
    public static SecurityException ofExpiredJWT(String message, Map<String, String> headers){
        return new SecurityException(EXPIRED_TOKEN, message, headers);
    }

    /**
     * JWT send by client has an invalid signature
     * @param message debug message
     * @param headers custom headers to send alongside the error payload
     * @return security exception of invalid JWT
     */
    public static SecurityException ofInvalidJWT(String message, Map<String, String> headers){
        return new SecurityException(INVALID_TOKEN, message, headers);
    }

    /**
     * Client tried to access restricted feature
     * @return security exception of insufficient permission
     */
    public static SecurityException ofInsufficientPermission(){
        return new SecurityException(HttpStatus.FORBIDDEN, INSUFFICIENT_PERMISSION, "Insufficient permission, access denied");
    }

    /**
     * Refresh token send by client is not up to date with the client last identity state stored in database
     * @return security exception of out of date token
     */
    public static SecurityException ofOutOfDateJWT(){
        return new SecurityException(OUT_OF_DATE_TOKEN, "User identity was updated, token out of date");
    }

    /**
     * When a stored hashed password has an incorrect format
     * @return technical exception of corrupted hashed password
     */
    public static TechnicalException ofCorruptedPassword(){
        return new TechnicalException(CORRUPTED_HASH, "Hashed password is invalid", null);
    }
}
