package com.solofeed.genesis.clock.api.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * Container for settings enabling the computation of a virtual date
 */
@Data
public class ClockSettingsDto {

    /** Real date where virtual date simulation will begin */
    private LocalDate start;

    /** Fake date where virtual date is starting */
    private LocalDate origin;

    /** Time speed factor */
    private Double factor;
}
