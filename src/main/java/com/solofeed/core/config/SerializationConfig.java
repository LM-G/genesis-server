package com.solofeed.core.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SerializationConfig {
    @Bean
    public Gson gsonBuilder(){
        return new Gson();
    }
}
