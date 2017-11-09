package com.solofeed.genesis.shared.user.mapper;

import com.solofeed.genesis.core.security.api.dto.UserSubjectDto;
import com.solofeed.genesis.shared.user.api.dto.UserDto;
import com.solofeed.genesis.shared.user.api.dto.CreateUserDto;
import com.solofeed.genesis.shared.user.domain.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User fromDto(UserDto dto);
    User fromCreateDto(CreateUserDto createDto);
    UserSubjectDto toBasicUserDto(UserDto userDto);

    List<UserDto> toDto(List<User> user);
    List<UserDto> fromDto(List<UserDto> user);
}
