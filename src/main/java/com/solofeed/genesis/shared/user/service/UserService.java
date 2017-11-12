package com.solofeed.genesis.shared.user.service;

import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.core.security.api.exception.AuthError;
import com.solofeed.genesis.core.security.service.PasswordEncoder;
import com.solofeed.genesis.shared.user.api.dto.CreateUserDto;
import com.solofeed.genesis.shared.user.api.dto.UserDto;
import com.solofeed.genesis.shared.user.dao.UserRepository;
import com.solofeed.genesis.shared.user.domain.User;
import com.solofeed.genesis.shared.user.exception.UserActionError;
import com.solofeed.genesis.shared.user.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService implements IUserService{
    @Inject
    private UserRepository userRepository;

    @Inject
    private UserMapper userMapper;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getUsers() {
        return userMapper.toDto(userRepository.findAll());
    }

    @Override
    public UserDto getUser() {
        UserDto user = new UserDto();
        user.setName("yolo");
        return user;
    }

    @Override
    public UserDto getUser(Long id) throws APIException{
        User user = userRepository.findOne(id);
        if(user == null){
            throw UserActionError.ofNotFound("user not found");
        }
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUser(String login, String password) throws APIException {
        // finds the user by name or email
        User user = userRepository.findByNameOrEmail(login, login);

        if(user == null){
            throw UserActionError.ofNotFound("user not found");
        }

        // checks the password,
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw AuthError.ofWrongCredentials();
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void createUser(CreateUserDto userDto) throws APIException{
        // try to find the user in database
        User user = userRepository.findByNameOrEmail(userDto.getName(), userDto.getEmail());

        // detail the error in case of conflict
        if(user != null){
            boolean nameAvailable = !StringUtils.equals(userDto.getName(), user.getName());
            boolean emailAvailable = !StringUtils.equals(userDto.getEmail(), user.getEmail());
            throw UserActionError.ofRegistrationFailed(nameAvailable, emailAvailable);
        }

        // initilize user entity from provided dto
        user = userMapper.fromCreateDto(userDto);

        // hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // and persist the user
        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        return userMapper.toDto(userRepository.save(user));
    }
}
