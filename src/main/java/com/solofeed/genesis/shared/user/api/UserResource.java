package com.solofeed.genesis.shared.user.api;

import com.github.fge.jsonpatch.JsonPatchException;
import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.core.security.decorator.Secured;
import com.solofeed.genesis.core.security.domain.CurrentUser;
import com.solofeed.genesis.shared.user.api.dto.UserDto;
import com.solofeed.genesis.shared.user.domain.Role;
import com.solofeed.genesis.shared.user.service.IUserService;
import com.solofeed.genesis.util.JsonPatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@RequiredArgsConstructor
@Secured
@Component
@Path("/users")
public class UserResource {

    private final IUserService userService;
    private final JsonPatcher jsonPatcher;
    private final Validator validator;

    @Context
    private SecurityContext context;

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
