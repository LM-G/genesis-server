package com.solofeed.genesis.shared.user;

import com.github.fge.jsonpatch.JsonPatchException;
import com.solofeed.genesis.core.config.JsonPatcher;
import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.shared.user.dto.UserDto;
import com.solofeed.genesis.shared.user.service.IUserService;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private IUserService userService;
    @Inject
    private JsonPatcher jsonPatcher;
    @Inject
    private Validator validator;

    @GET
    @Path("me")
    public UserDto getUser() throws APIException{
        return userService.getUser();
    }

    @GET
    public List<UserDto> getUsers(){
        return userService.getUsers();
    }


    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON_PATCH_JSON)
    public UserDto updateUser(@PathParam("id") Long id, String operations) throws APIException{
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
