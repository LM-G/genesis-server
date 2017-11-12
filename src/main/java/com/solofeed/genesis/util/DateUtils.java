package com.solofeed.genesis.util;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Date transformation utilities
 */
public final class DateUtils {
    /** private constructor */
    private DateUtils(){
        // no-op
    }

    /**
     * Transform a {@link LocalDateTime} with an associated {@link ZoneId} to a {@link Date}
     */
    public static Date toDate(@NonNull LocalDateTime date, @NonNull ZoneId zone) {
        return Date.from(date.atZone(zone).toInstant());
    }
}
