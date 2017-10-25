package com.solofeed.shared.user.exception;

import com.solofeed.core.exception.APIException;
import com.solofeed.core.exception.FunctionalException;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * User shared feature related exceptions
 */
public class UserException {
    private static final String REGISTRATION_FAILED = "E_USER_REGISTRATION_FAILED";

    /**
     * Create an user registration exception with field which are in conflict with database state
     * @param detail contains name and email availability state
     * @return user registration exception
     */
    public static APIException ofRegistrationFailed(Map<String, String> detail){
        return new FunctionalException(HttpStatus.CONFLICT, REGISTRATION_FAILED, "User registration failed", detail);
    }
}
