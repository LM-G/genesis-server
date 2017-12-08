package com.solofeed.genesis.clock.api;

import com.solofeed.genesis.clock.api.dto.ClockSettingsDto;
import com.solofeed.genesis.clock.domain.DateProgression;
import com.solofeed.genesis.clock.service.ClockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Clock resource
 */
@Path("/clock")
@Component
@RequiredArgsConstructor
public class ClockResource {
    /** clock service */
    private final ClockService clockService;

    /**
     * Gets virtual clock
     * @return virutal clock
     */
    @GET
    public DateProgression getVirtualDate() {
        return clockService.getVirtualClock();
    }

    /**
     * Gets clock settings
     * @return clock settings
     */
    @GET
    @Path("/settings")
    public ClockSettingsDto getSettings() {
        return clockService.getSettings();
    }
}
