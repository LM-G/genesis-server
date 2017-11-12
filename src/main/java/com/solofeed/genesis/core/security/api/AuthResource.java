package com.solofeed.genesis.core.security.api;

import com.solofeed.genesis.core.exception.APIException;
import com.solofeed.genesis.core.security.api.dto.SignInDto;
import com.solofeed.genesis.core.security.api.dto.TokensDto;
import com.solofeed.genesis.core.security.api.dto.UserTokenDto;
import com.solofeed.genesis.core.security.api.exception.AuthError;
import com.solofeed.genesis.core.security.decorator.Secured;
import com.solofeed.genesis.core.security.service.TokenService;
import com.solofeed.genesis.shared.user.api.dto.CreateUserDto;
import com.solofeed.genesis.shared.user.api.dto.UserDto;
import com.solofeed.genesis.shared.user.service.IUserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Authentication resource
 */
@Component @Path("/auth")
@RequiredArgsConstructor
public class AuthResource {

    /** Users handling service */
    private final IUserService userService;

    /** Tokens creation factory */
    private final TokenService tokenService;

    /**
     * Requires a new set of tokens
     * @param refreshToken refresh token
     * @return new access token and updated refresh token
     */
    @GET
    @Path("/refresh")
    public TokensDto refresh(@NonNull @QueryParam("token") String refreshToken) throws APIException {
        // gets the user from token
        UserTokenDto userFromToken = tokenService.extractUser(refreshToken);
        String identifier = tokenService.getIdentifier(refreshToken);
        // gets the user from database
        UserDto user = userService.getUser(userFromToken.getId());

        // checks if the user still has the right to be granted a fresh access token
        if(!StringUtils.equals(user.getState(), identifier)) {
            throw AuthError.ofOutOfDateJWT();
        }

        return tokenService.refresh(refreshToken);
    }

    /**
     * Login action
     * @param form login form with login/password
     * @return access and refresh tokens holder
     * @throws APIException if user can't be found
     */
    @POST
    @Path("/sign-in")
    public TokensDto login(@Valid SignInDto form) throws APIException {
        // gets the user
        UserDto user = userService.getUser(form.getLogin(), form.getPassword());

        // generate JWT token
        return tokenService.createTokens(user);
    }

    /**
     * Registration action
     * @param form registration form
     * @throws APIException if user already exists
     */
    @POST
    @Path("/sign-up")
    public void register(@Valid CreateUserDto form) throws APIException{
        userService.createUser(form);
    }

    @POST @Secured
    @Path("/sign-out")
    public void logout(SignInDto form){
        throw new NotImplementedException("wip");
    }
}
