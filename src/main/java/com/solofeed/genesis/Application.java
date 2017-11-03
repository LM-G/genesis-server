package com.solofeed.genesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
    JacksonAutoConfiguration.class // remove jackson from classpath
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}