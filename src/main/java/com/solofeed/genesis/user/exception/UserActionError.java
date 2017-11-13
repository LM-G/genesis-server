package com.solofeed.genesis.user.exception;

import com.solofeed.genesis.core.exception.FunctionalException;
import com.solofeed.genesis.core.exception.GenericError;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * User shared feature related exceptions
 */
public class UserActionError extends GenericError {
    public static final String NOT_FOUND = "E_USER_NOT_FOUND";
    public static final String REGISTRATION_FAILED = "E_USER_REGISTRATION_FAILED";

    /**
     * Create a user registration exception with field which are in conflict with database state
     * @param name name availability
     * @param email email availability
     * @return functional exception of failed registration
     */
    public static FunctionalException ofRegistrationFailed(boolean name, boolean email){
        Map<String, String> detail = new HashMap();
        detail.put("name", Boolean.toString(name));
        detail.put("email", Boolean.toString(email));
        return new FunctionalException(HttpStatus.CONFLICT, REGISTRATION_FAILED, "User registration failed", detail);
    }

    /**
     * Create a user not found exception
     * @param message debug message
     * @return function exception of not found user
     */
    public static FunctionalException ofNotFound(String message){
        return ofNotFound(message, NOT_FOUND);
    }
}