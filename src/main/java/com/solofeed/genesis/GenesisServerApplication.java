package com.solofeed.genesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class GenesisServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GenesisServerApplication.class, args);
	}
}
