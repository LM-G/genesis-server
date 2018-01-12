package com.solofeed.genesis.core.event;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Event triggering a player state calculation
 */
@Data
public class PlayerUpdateEvent {
    /** Player to update */
    private Long playerId;
    /** Date of event creation */
    private LocalDateTime date;
}
