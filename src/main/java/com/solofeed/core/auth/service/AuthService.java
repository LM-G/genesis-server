package com.solofeed.core.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.solofeed.core.auth.exception.AuthException;
import com.solofeed.shared.user.dto.BasicUserDto;
import com.solofeed.shared.user.dto.UserDto;
import com.solofeed.shared.user.mapper.UserMapper;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.io.IOException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;

/**
 * Created by LM-G on 25/10/2017.
 */
@Service
public class AuthService {
    @Inject
    private Gson gson;
    @Inject
    private UserMapper userMapper;
    // temp
    @Value("${jwt.token.id}")
    private String ID;
    @Value("${jwt.token.issuer}")
    private String ISSUER;
    @Value("${jwt.token.secret}")
    private String SECRET;
    @Value("${jwt.expire.hours}")
    private int EXPIRATION_HOURS;
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    private Key getKey(){
        byte[] encodedSecret = Base64.getEncoder().encode(SECRET.getBytes());
        return new SecretKeySpec(encodedSecret, ALGORITHM.getJcaName());
    }

    public String createToken(UserDto userDto) {
        BasicUserDto basicUserDto = userMapper.toBasicUserDto(userDto);
        String userJson = gson.toJson(basicUserDto);
        LocalDateTime now = LocalDateTime.now();
        Key signingKey = getKey();
        // jwt claim setting
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(ID)
                .setIssuedAt(Date.from(now.toInstant(ZoneOffset.UTC)))
                .setSubject(userJson)
                .setIssuer(ISSUER)
                .signWith(ALGORITHM, signingKey)
                .setExpiration(Date.from(now.plusHours(EXPIRATION_HOURS).toInstant(ZoneOffset.UTC)));

        return jwtBuilder.compact();
    }

    public UserDto extractUserFromToken(String token) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Jws<Claims> claims = Jwts.parser().setSigningKey(getKey()).parseClaimsJws(token);
        String stringifiedUser = claims.getBody().getSubject();
        return objectMapper.readValue(stringifiedUser, UserDto.class);
    }
}
