package com.solofeed.service;

import com.solofeed.dto.UserDto;
import com.solofeed.model.User;

import java.util.List;

public interface IUserService {
    List<User> getUsers();

    User getUser(Long id);

    User updateUser(UserDto userDto);
}
