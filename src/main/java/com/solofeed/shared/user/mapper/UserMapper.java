package com.solofeed.shared.user.mapper;

import com.solofeed.shared.user.dto.UserDto;
import com.solofeed.shared.user.dto.CreateUserDto;
import com.solofeed.shared.user.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User fromDto(UserDto dto);
    User fromCreateDto(CreateUserDto createDto);

    List<UserDto> toDto(List<User> user);
    List<UserDto> fromDto(List<UserDto> user);
}
