package com.solofeed.genesis.shared.user.mapper;

import com.solofeed.genesis.shared.user.dto.BasicUserDto;
import com.solofeed.genesis.shared.user.dto.UserDto;
import com.solofeed.genesis.shared.user.dto.CreateUserDto;
import com.solofeed.genesis.shared.user.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User fromDto(UserDto dto);
    User fromCreateDto(CreateUserDto createDto);
    BasicUserDto toBasicUserDto(UserDto userDto);

    List<UserDto> toDto(List<User> user);
    List<UserDto> fromDto(List<UserDto> user);
}
