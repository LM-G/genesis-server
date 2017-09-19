package com.solofeed.mapper;

import com.solofeed.dto.UserDto;
import com.solofeed.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User fromDto(UserDto dto);
}
