package com.solofeed.service;

import com.solofeed.dto.UserDto;
import com.solofeed.dto.form.CreateUserDto;
import com.solofeed.model.User;

import javax.ws.rs.NotFoundException;
import java.util.List;

public interface IUserService {
    List<UserDto> getUsers();

    UserDto getUser(Long id) throws NotFoundException;

    UserDto updateUser(UserDto userDto);

    void createUser(CreateUserDto form);
}
