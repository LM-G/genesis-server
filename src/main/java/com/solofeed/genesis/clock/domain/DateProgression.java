package com.solofeed.genesis.clock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DateProgression {
    private LocalDateTime date;
    private Long progress;
}
