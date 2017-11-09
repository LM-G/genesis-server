package com.solofeed.genesis.shared.user.service;

import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.core.security.service.PasswordEncoder;
import com.solofeed.genesis.core.security.api.exception.AuthException;
import com.solofeed.genesis.shared.user.dao.UserRepository;
import com.solofeed.genesis.shared.user.api.dto.CreateUserDto;
import com.solofeed.genesis.shared.user.api.dto.UserDto;
import com.solofeed.genesis.shared.user.exception.UserException;
import com.solofeed.genesis.shared.user.mapper.UserMapper;
import com.solofeed.genesis.shared.user.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
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
    public UserDto getUser() throws NotFoundException{
        UserDto user = new UserDto();
        user.setName("yolo");
        return user;
    }

    @Override
    public UserDto getUser(Long id) throws APIException{
        User user = userRepository.findOne(id);
        if(user == null){
            throw UserException.ofNotFound("user not found");
        }
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUser(String login, String password) throws APIException {
        // finds the user by name or email
        User user = userRepository.findByNameOrEmail(login, login);

        if(user == null){
            throw UserException.ofNotFound("user not found");
        }

        // checks the password,
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw AuthException.ofWrongCredentials();
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void createUser(CreateUserDto form) throws APIException{
        User user = userRepository.findByNameOrEmail(form.getName(), form.getEmail());

        // detail the error
        if(user != null){
            boolean nameAvailable = !StringUtils.equals(form.getName(), user.getName());
            boolean emailAvailable = !StringUtils.equals(form.getEmail(), user.getEmail());
            throw UserException.ofRegistrationFailed(nameAvailable, emailAvailable);
        }

        user = userMapper.fromCreateDto(form);

        // hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        return userMapper.toDto(userRepository.save(user));
    }
}
