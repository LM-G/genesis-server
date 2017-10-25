package com.solofeed.core.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solofeed.shared.user.dto.UserDto;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
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

    public String createToken(UserDto userDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        LocalDateTime now = LocalDateTime.now();
        Key signingKey = getKey();
        //Let's set the JWT Claims
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId(ID)
                .setIssuedAt(Date.from(now.toInstant(ZoneOffset.UTC)))
                .setSubject(objectMapper.writeValueAsString(userDto))
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

    /*
    //Sample method to validate and read the JWT
    private void parseJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey.getSecret()))
                .parseClaimsJws(jwt).getBody();
        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiration: " + claims.getExpiration());
    }*/
}
