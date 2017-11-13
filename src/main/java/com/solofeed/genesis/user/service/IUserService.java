package com.solofeed.genesis.user.service;

import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.user.api.dto.CreateUserDto;
import com.solofeed.genesis.user.api.dto.UserDto;

import java.util.List;

public interface IUserService {
    List<UserDto> getUsers();

    UserDto getUser() throws APIException;

    UserDto getUser(Long id) throws APIException;

    UserDto getUser(String login, String password) throws APIException;

    UserDto updateUser(UserDto userDto);

    /**
     * User creation
     * @param userDto user to create
     * @throws APIException in case of conflict with another existing user
     */
    void createUser(CreateUserDto userDto) throws APIException;
}
