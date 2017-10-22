package com.solofeed.shared.user;

import com.solofeed.core.exception.APIException;
import com.solofeed.shared.user.dto.CreateUserDto;
import com.solofeed.shared.user.dto.UserDto;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.List;

public interface IUserService {
    List<UserDto> getUsers();

    UserDto getUser(Long id) throws NotFoundException;

    UserDto getUser(String login, String password) throws APIException;

    UserDto updateUser(UserDto userDto);

    void createUser(CreateUserDto form) throws BadRequestException;
}
