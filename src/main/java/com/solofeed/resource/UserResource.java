package com.solofeed.resource;

import com.github.fge.jsonpatch.JsonPatchException;
import com.solofeed.config.JsonPatcher;
import com.solofeed.dto.UserDto;
import com.solofeed.dto.form.CreateUserDto;
import com.solofeed.mapper.UserMapper;
import com.solofeed.model.User;
import com.solofeed.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    /** User endpoint logger */
    private final static Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private IUserService userService;
    @Inject
    private UserMapper userMapper;
    @Inject
    private JsonPatcher jsonPatcher;
    @Inject
    private Validator validator;

    @GET
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }

    @POST
    public void registerUser(CreateUserDto form){
        userService.createUser(form);
    }


    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON_PATCH_JSON)
    public UserDto updateUser(@PathParam("id") Long id, String operations) throws NotFoundException{
        UserDto userDto = userService.getUser(id);

        Optional<UserDto> patchUserDto = Optional.empty();
        try {
            patchUserDto = jsonPatcher.patch(operations, userDto);
        } catch (RuntimeException e) {
            if (JsonPatchException.class.isAssignableFrom(e.getCause().getClass())) {
                throw new BadRequestException("Mise à jour impossible, instructions incorrect");
            }
        }

        if(patchUserDto.isPresent()){
            Set<ConstraintViolation<UserDto>> violations =  validator.validate(patchUserDto.get());
            if(!violations.isEmpty()){
                throw new ConstraintViolationException(violations);
            }
        } else {
            throw new BadRequestException("Rien à patcher");
        }

        return userService.updateUser(patchUserDto.get());
    }
}
