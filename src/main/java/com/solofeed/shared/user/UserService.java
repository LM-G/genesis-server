package com.solofeed.shared.user;

import com.solofeed.core.exception.APIException;
import com.solofeed.shared.auth.exception.AuthException;
import com.solofeed.shared.user.dao.UserRepository;
import com.solofeed.shared.user.dto.CreateUserDto;
import com.solofeed.shared.user.dto.UserDto;
import com.solofeed.shared.user.mapper.UserMapper;
import com.solofeed.shared.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
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
    public UserDto getUser(Long id) throws NotFoundException{
        User user = userRepository.findOne(id);
        if(user == null){
            throw new NotFoundException("user not found");
        }
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUser(String login, String password) throws APIException {
        String hashedPassword = passwordEncoder.encode(password);
        User user = userRepository.findByPasswordAndNameOrEmail(hashedPassword, login, login);
        if(user == null){
            throw AuthException.ofWrongCredentials();
        }
        UserDto result = userMapper.toDto(user);
        // TODO JWT
        return result; // temp
    }

    @Override
    @Transactional
    public void createUser(CreateUserDto form) throws BadRequestException{
        User user = userRepository.findByNameOrEmail(form.getName(), form.getEmail());

        // detail the error
        if(user != null){
            StringBuilder sb = new StringBuilder();
            if(StringUtils.equals(form.getEmail(), user.getEmail())){
                sb.append("email");
            }
            if(StringUtils.equals(form.getName(), user.getName())){
                if(sb.length() > 0) {
                    sb.append(" and ");
                }
                sb.append("username");
            }
            sb.append(" not available");
            throw new BadRequestException(sb.toString());
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
