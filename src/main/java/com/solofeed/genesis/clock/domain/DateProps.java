package com.solofeed.genesis.clock.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;


/**
 * Properties enabling the computation of a virtual date
 */
@Data
@Configuration
@PropertySource("classpath:functional.properties")
@ConfigurationProperties(prefix = "date")
public class DateProps {

    /** Real date where virtual date simulation will begin */
    private LocalDate start;

    /** Fake date where virtual date is starting */
    private LocalDate origin;

    /** Time speed factor */
    private Double factor;
}
