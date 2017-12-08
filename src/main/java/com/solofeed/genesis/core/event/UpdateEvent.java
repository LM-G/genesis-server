package com.solofeed.genesis.core.event;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateEvent {
    private Long tick;

    public static UpdateEvent of(@NonNull Long tick) {
        UpdateEvent event = new UpdateEvent();
        event.setTick(tick);
        return event;
    }
}
