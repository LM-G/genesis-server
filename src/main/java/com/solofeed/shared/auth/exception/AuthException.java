package com.solofeed.shared.auth.exception;

import com.solofeed.core.exception.APIException;
import com.solofeed.core.exception.FunctionalException;
import org.springframework.http.HttpStatus;

/**
 * Created by LM-G on 22/10/2017.
 */
public class AuthException {
    public static APIException ofWrongCredentials(){
        return FunctionalException.with(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "E_WRONG_CREDENTIALS",
                "Authentication failed, wrong credentials",
                null);
    }
}
