package com.solofeed.mapper;

import com.solofeed.dto.UserDto;
import com.solofeed.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User fromDto(UserDto dto);

    List<UserDto> toDto(List<User> user);
    List<UserDto> fromDto(List<UserDto> user);
}
