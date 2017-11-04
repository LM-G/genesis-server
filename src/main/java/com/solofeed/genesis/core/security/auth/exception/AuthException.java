package com.solofeed.genesis.core.security.auth.exception;

import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.core.exception.FunctionalException;
import com.solofeed.genesis.core.exception.SecurityException;
import com.solofeed.genesis.core.exception.TechnicalException;
import org.springframework.http.HttpStatus;

public class AuthException {
    private static final String WRONG_CREDENTIALS = "E_WRONG_CREDENTIALS";
    private static final String TOKEN_CREATION = "E_TOKEN_CREATION";
    private static final String MISSING_TOKEN = "E_MISSING_TOKEN";

    public static APIException ofWrongCredentials(){
        return new FunctionalException(HttpStatus.UNAUTHORIZED,WRONG_CREDENTIALS, "Authentication failed, wrong credentials");
    }

    public static TechnicalException ofTokenCreationFailed(Throwable cause){
        return new TechnicalException(TOKEN_CREATION, "Token creation failed", cause);
    }

    public static SecurityException ofMissingJWT(){
        return new SecurityException(MISSING_TOKEN, "JWT is missing");
    }
}
