package com.solofeed.genesis.core.exception;

import com.solofeed.genesis.core.exception.model.FunctionalException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

/**
 * Generic error builder
 */
@UtilityClass
public class GenericError {
    /**
     * When resource is not found
     * @param code functional custom code
     * @param message debug message
     * @return function exception of resource not found with http status 404
     */
    public static FunctionalException ofNotFound(String code , String message){
        return new FunctionalException(HttpStatus.NOT_FOUND, code, message);
    }
}
