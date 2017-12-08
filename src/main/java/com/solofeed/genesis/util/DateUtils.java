package com.solofeed.genesis.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Date transformation utilities
 */
@UtilityClass
public final class DateUtils {
    /**
     * Transform a {@link LocalDateTime} with an associated {@link ZoneId} to a {@link Date}
     */
    public static Date toDate(@NonNull LocalDateTime date, @NonNull ZoneId zone) {
        return Date.from(date.atZone(zone).toInstant());
    }
}
