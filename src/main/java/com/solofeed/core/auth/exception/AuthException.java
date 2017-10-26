package com.solofeed.core.auth.exception;

import com.solofeed.core.exception.APIException;
import com.solofeed.core.exception.FunctionalException;
import com.solofeed.core.exception.TechnicalException;
import org.springframework.http.HttpStatus;

/**
 * Created by LM-G on 22/10/2017.
 */
public class AuthException {
    private static final String WRONG_CREDENTIALS = "E_WRONG_CREDENTIALS";
    private static final String TOKEN_CREATION = "E_TOKEN_CREATION";

    public static APIException ofWrongCredentials(){
        return new FunctionalException(HttpStatus.UNAUTHORIZED,WRONG_CREDENTIALS, "Authentication failed, wrong credentials");
    }

    public static APIException ofTokenCreationFailed(Throwable cause){
        return new TechnicalException(TOKEN_CREATION, "Token creation failed", cause);
    }
}
