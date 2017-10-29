package com.solofeed.genesis.shared.user.service;

import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.shared.user.dto.CreateUserDto;
import com.solofeed.genesis.shared.user.dto.UserDto;

import java.util.List;

public interface IUserService {
    List<UserDto> getUsers();

    UserDto getUser() throws APIException;

    UserDto getUser(Long id) throws APIException;

    UserDto getUser(String login, String password) throws APIException;

    UserDto updateUser(UserDto userDto);

    void createUser(CreateUserDto form) throws APIException;
}
