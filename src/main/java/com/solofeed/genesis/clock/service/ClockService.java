package com.solofeed.genesis.clock.service;

import com.solofeed.genesis.clock.api.dto.ClockSettingsDto;
import com.solofeed.genesis.clock.domain.DateProgression;
import com.solofeed.genesis.clock.domain.DateProps;
import com.solofeed.genesis.clock.mapper.ClockMapper;
import com.solofeed.genesis.core.event.UpdateEvent;
import com.solofeed.genesis.core.event.decorator.Every;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * Clock computation servvice
 */
@Service
@RequiredArgsConstructor
public class ClockService {

    /** Virutal date properties */
    private final DateProps dateProps;

    /** Clock mapper */
    private final ClockMapper clockMapper;

    private DateProgression dateProgression;

    @Every(10)
    @EventListener
    public void onUpdate(UpdateEvent event){
        dateProgression = calculate(LocalDateTime.now());
    }

    /**
     * Gets clock settings
     * @return clock settings
     */
    public ClockSettingsDto getSettings(){
        return clockMapper.toDto(dateProps);
    }

    /**
     * Gets virtual clock
     * @return virtual clock
     */
    public DateProgression getVirtualClock(){
        return dateProgression;
    }

    private DateProgression calculate(LocalDateTime date){
        long delta = date.toInstant(ZoneOffset.UTC).toEpochMilli()
            - LocalDateTime.of(dateProps.getStart(), LocalTime.MIDNIGHT).toInstant(ZoneOffset.UTC).toEpochMilli();

        double result = LocalDateTime.of(dateProps.getOrigin(), LocalTime.MIDNIGHT).toInstant(ZoneOffset.UTC).toEpochMilli()
            + delta * dateProps.getFactor();

        LocalDateTime virtualDate = LocalDateTime.ofInstant(Instant.ofEpochMilli((long)result), ZoneOffset.UTC).withNano(0);
        long progression = (virtualDate.getHour() * 60 + virtualDate.getMinute()) * 100 / 1440;

        return new DateProgression(virtualDate, progression);
    }
}
