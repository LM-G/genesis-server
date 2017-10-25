package com.solofeed.shared.auth.exception;

import com.solofeed.core.exception.APIException;
import com.solofeed.core.exception.FunctionalException;
import org.springframework.http.HttpStatus;

/**
 * Created by LM-G on 22/10/2017.
 */
public class AuthException {
    private static final String WRONG_CREDENTIALS = "E_WRONG_CREDENTIALS";

    public static APIException ofWrongCredentials(){
        return new FunctionalException(HttpStatus.UNPROCESSABLE_ENTITY,WRONG_CREDENTIALS,
                "Authentication failed, wrong credentials", null);
    }
}
