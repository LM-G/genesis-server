package com.solofeed.service.impl;

import com.solofeed.dao.UserRepository;
import com.solofeed.dto.UserDto;
import com.solofeed.mapper.UserMapper;
import com.solofeed.model.User;
import com.solofeed.service.IUserService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class UserService implements IUserService{
    @Inject
    private UserRepository userRepository;

    @Inject
    private UserMapper userMapper;

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User updateUser(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        return userRepository.save(user);
    }
}
