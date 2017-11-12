package com.solofeed.genesis.core.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public interface IAPIException {
    String getCode();
    HttpStatus getStatus();
    APIExceptionNature getNature();
    Map<String, String> getHeaders();


    enum APIExceptionNature {
        FUNCTIONAL,
        TECHNICAL,
        SECURITY
    }
}
