package com.solofeed.service.impl;

import com.solofeed.dao.UserRepository;
import com.solofeed.dto.UserDto;
import com.solofeed.dto.form.CreateUserDto;
import com.solofeed.mapper.UserMapper;
import com.solofeed.model.User;
import com.solofeed.service.IUserService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class UserService implements IUserService{
    @Inject
    private UserRepository userRepository;

    @Inject
    private UserMapper userMapper;

    @Override
    public List<UserDto> getUsers() {
        return userMapper.toDto(userRepository.findAll());
    }

    @Override
    public UserDto getUser(Long id) throws NotFoundException{
        User user = userRepository.findOne(id);
        if(user == null){
            throw new NotFoundException("user not found");
        }
        return userMapper.toDto(user);
    }

    @Override
    public void createUser(CreateUserDto form) {

    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        return userMapper.toDto(userRepository.save(user));
    }
}
