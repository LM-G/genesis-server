package com.solofeed.genesis.echo.service;

import org.springframework.stereotype.Service;

@Service
public class EchoService {
    public String getMessage(String message) {
        return message;
    }
}
