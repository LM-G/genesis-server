package com.solofeed.genesis.clock.mapper;

import com.solofeed.genesis.clock.api.dto.ClockSettingsDto;
import com.solofeed.genesis.clock.domain.DateProps;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClockMapper {
    /** Converts {@link DateProps} to {@link ClockSettingsDto} */
    ClockSettingsDto toDto(DateProps dateProperties);
}
