package com.solofeed.genesis.user.service;

import com.solofeed.genesis.core.exception.model.APIException;
import com.solofeed.genesis.core.security.api.exception.AuthError;
import com.solofeed.genesis.core.security.service.PasswordEncoder;
import com.solofeed.genesis.core.AppState;
import com.solofeed.genesis.user.api.dto.CreateUserDto;
import com.solofeed.genesis.user.api.dto.UserDto;
import com.solofeed.genesis.user.dao.UserRepository;
import com.solofeed.genesis.user.domain.User;
import com.solofeed.genesis.user.exception.UserActionError;
import com.solofeed.genesis.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User handling service
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    /** User data access */
    private final UserRepository userRepository;

    /** User entity and sub DTO mapper */
    private final UserMapper userMapper;

    /** Password encoder component */
    private final PasswordEncoder passwordEncoder;

    /** Application state */
    private final AppState appState;

    /**
     * Get user information
     *
     * @param id user id
     * @return user data
     * @throws APIException if user not found
     */
    public UserDto getUser(Long id) throws APIException{
        return Optional.ofNullable(userRepository.findOne(id))
            .map(userMapper::toDto)
            .orElseThrow(() -> UserActionError.ofNotFound("User not found"));
    }

    /**
     * Get user information based on his name or mail and password
     *
     * @param login user's name or mail
     * @param password user's password
     * @return user data
     * @throws APIException if user not found or wrong password provided
     */
    public UserDto getUser(String login, String password) throws APIException {
        // finds the user by name or email
        UserDto userDto = Optional.ofNullable(userRepository.findByNameOrEmail(login, login))
            .map(userMapper::toDto)
            .orElseThrow(() -> UserActionError.ofNotFound("User not found"));

        // checks the password,
        if(!passwordEncoder.matches(password, userDto.getPassword())){
            throw AuthError.ofWrongCredentials();
        }

        return userDto;
    }

    /**
     * Get the list of connected users
     *
     * @return list of connected users
     */
    public List<UserDto> getConnectedUsers() {
        return appState.getConnectedUsers();
    }

    /**
     * Create a new user on database
     *
     * @param userDto user creation form
     * @throws APIException if user already exists in database
     */
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
}
