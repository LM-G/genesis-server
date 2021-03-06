package com.solofeed.genesis.user.mapper;

import com.solofeed.genesis.core.security.api.dto.UserTokenDto;
import com.solofeed.genesis.user.api.dto.UserDto;
import com.solofeed.genesis.user.api.dto.CreateUserDto;
import com.solofeed.genesis.user.domain.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User fromDto(UserDto dto);
    User fromCreateDto(CreateUserDto createDto);
    UserTokenDto toSubjectDto(UserDto userDto);

    List<UserDto> toDto(List<User> user);
    List<User> fromDto(List<UserDto> user);
}
