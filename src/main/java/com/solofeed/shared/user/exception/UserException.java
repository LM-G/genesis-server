package com.solofeed.shared.user.exception;

import com.solofeed.core.exception.APIException;
import com.solofeed.core.exception.FunctionalException;
import com.solofeed.core.exception.GenericException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * User shared feature related exceptions
 */
public class UserException extends GenericException {
    public static final String NOT_FOUND = "E_USER_NOT_FOUND";
    public static final String REGISTRATION_FAILED = "E_USER_REGISTRATION_FAILED";

    /**
     * Create a user registration exception with field which are in conflict with database state
     * @param name name availability
     * @param email email availability
     * @returnuser registration exception
     */
    public static APIException ofRegistrationFailed(boolean name, boolean email){
        Map<String, String> detail = new HashMap();
        detail.put("name", Boolean.toString(name));
        detail.put("email", Boolean.toString(email));
        return new FunctionalException(HttpStatus.CONFLICT, REGISTRATION_FAILED, "User registration failed", detail);
    }

    public static APIException ofNotFound(String message){
        return ofNotFound(message, NOT_FOUND);
    }
}
