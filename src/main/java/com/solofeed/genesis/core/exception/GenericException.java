package com.solofeed.genesis.core.exception;

import org.springframework.http.HttpStatus;

public abstract class GenericException {
    protected static FunctionalException ofNotFound(String message, String code){
        return new FunctionalException(HttpStatus.NOT_FOUND, code, message);
    }
}
