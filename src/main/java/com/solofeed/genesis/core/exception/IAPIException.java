package com.solofeed.genesis.core.exception;

import org.springframework.http.HttpStatus;

public interface IAPIException {
    String getCode();
    HttpStatus getStatus();
    APIExceptionNature getNature();


    enum APIExceptionNature {
        FUNCTIONAL,
        TECHNICAL,
        SECURITY
    }
}
