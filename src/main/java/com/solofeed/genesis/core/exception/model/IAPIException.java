package com.solofeed.genesis.core.exception.model;

import org.springframework.http.HttpStatus;

import java.util.Map;

public interface IAPIException {
    String getCode();
    HttpStatus getStatus();
    Map<String, String> getHeaders();
}
