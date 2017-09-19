package com.solofeed.controller;

import com.github.fge.jsonpatch.JsonPatchException;
import com.solofeed.config.JsonPatcher;
import com.solofeed.dto.UserDto;
import com.solofeed.exception.UnprocessableEntityException;
import com.solofeed.mapper.UserMapper;
import com.solofeed.model.User;
import com.solofeed.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("users")
public class UserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Inject
    private IUserService userService;

    @Inject
    private UserMapper userMapper;
    @Inject
    private JsonPatcher jsonPatcher;
    @Inject
    private Validator validator;

    private static final String USER_NOT_FOUND = "User not found";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON_PATCH_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserDto updateUser(@PathParam("id") Long id, String operations) {

        User user = userService.getUser(id);
        if(user == null){
            throw new NotFoundException(USER_NOT_FOUND);
        }

        // user to dto
        UserDto userDto = userMapper.toDto(user);
        Optional<UserDto> patchUserDto = Optional.empty();
        try {
            patchUserDto = jsonPatcher.patch(operations, userDto);
        } catch (RuntimeException e) {
            if (JsonPatchException.class.isAssignableFrom(e.getCause().getClass())) {
                throw new BadRequestException("Mise à jour impossible, instructions incorrect");
            }
        }

        if(patchUserDto.isPresent()){
            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(patchUserDto.get(), "patchUserDto");
            validator.validate(patchUserDto.get(), errors);
            if(errors.hasErrors()){
                throw new UnprocessableEntityException();
            }
        } else {
            throw new BadRequestException("Rien à patcher");
        }

        return userMapper.toDto(userService.updateUser(patchUserDto.get()));
    }
}
