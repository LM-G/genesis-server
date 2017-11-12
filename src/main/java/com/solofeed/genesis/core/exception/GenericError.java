package com.solofeed.genesis.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Generic error builder
 */
public abstract class GenericError {
    /** hidden constructor */
    protected GenericError(){
        // no-op
    }

    /**
     * When resource is not found
     * @param message debug message
     * @param code functional custom code
     * @return function exception of resource not found
     */
    protected static FunctionalException ofNotFound(String message, String code){
        return new FunctionalException(HttpStatus.NOT_FOUND, code, message);
    }
}
