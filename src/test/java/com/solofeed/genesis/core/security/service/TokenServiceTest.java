package com.solofeed.genesis.core.security.service;

import com.google.gson.Gson;
import com.solofeed.genesis.Application;
import com.solofeed.genesis.core.security.api.dto.TokensDto;
import com.solofeed.genesis.core.security.api.dto.UserTokenDto;
import com.solofeed.genesis.core.security.domain.SecurityProps;
import com.solofeed.genesis.user.api.dto.UserDto;
import com.solofeed.genesis.user.domain.Role;
import com.solofeed.genesis.user.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.security.Key;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test {@link TokenService}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {
    Application.class
})
public class TokenServiceTest {
    private static final String SECRET = "foo";
    private static final String ISSUER = "foobar";
    private static final long EXPIRE_MINUTE = 1L;
    private static final long EXPIRE_MONTH = 1L;
    private static final String ALGORITHM = SignatureAlgorithm.HS256.getValue();
    public static final long ACCEPTABLE_OFFSET = 2000L;

    private TokenService tokenService;

    private Key key;
    private Claims claims;
    private String signedJwt;
    private UserTokenDto user;
    private Date expiresIn;

    @Inject
    private Gson gson;

    @Inject
    private UserMapper userMapper;

    private SecurityProps securityProps = new SecurityProps();

    @Before
    public void setUp(){
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, (int) EXPIRE_MINUTE);
        expiresIn = now.getTime();

        key = generateKey(SECRET);
        user = new UserTokenDto();
        user.setId(1L);
        user.setName("foo");
        user.setRole(Role.SIMPLE);
        claims = Jwts.claims()
            .setIssuer(ISSUER)
            .setSubject(gson.toJson(user))
            .setId("foo")
            .setExpiration(expiresIn);

        signedJwt = Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, key)
            .compact();
        securityProps.setKey(key);
        securityProps.setIssuer(ISSUER);
        securityProps.setAlgorithm(SignatureAlgorithm.HS512.getValue());
        securityProps.setExpireInMinutes((int) EXPIRE_MINUTE);
        securityProps.setExpireInMonth((int) EXPIRE_MONTH);

        tokenService = new TokenService(gson, userMapper, securityProps);
    }

    @Test
    public void shouldCreateTokens() throws Exception {
        // init
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setRole(user.getRole());
        String jsonifiedUser = gson.toJson(user);
        userDto.setState("foo");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, (int) EXPIRE_MONTH);
        Date inOneMonth = cal.getTime();
        Date now = new Date();

        // execution
        TokensDto tokensHolder = tokenService.createTokens(userDto);

        // assertions
        assertThat(tokensHolder).isNotNull();

        // access token
        String accessToken = tokensHolder.getAccessToken();
        assertThat(accessToken).isNotBlank();
        Jws<Claims> claimsAccessToken = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(accessToken);

        assertThat(claimsAccessToken.getBody().getIssuedAt()).isCloseTo(now, ACCEPTABLE_OFFSET);
        assertThat(claimsAccessToken.getBody().getExpiration()).isCloseTo(expiresIn, ACCEPTABLE_OFFSET);
        assertThat(claimsAccessToken.getBody().getSubject()).isEqualTo(jsonifiedUser);
        assertThat(claimsAccessToken.getBody().getIssuer()).isEqualTo(ISSUER);

        // refresh token
        String refreshToken = tokensHolder.getRefreshToken();
        assertThat(refreshToken).isNotBlank();
        Jws<Claims> claimsRefreshToken = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(refreshToken);


        assertThat(claimsRefreshToken.getBody().getIssuedAt()).isCloseTo(now, ACCEPTABLE_OFFSET);
        assertThat(claimsRefreshToken.getBody().getExpiration()).isCloseTo(inOneMonth, ACCEPTABLE_OFFSET);
        assertThat(claimsRefreshToken.getBody().getSubject()).isEqualTo(jsonifiedUser);
        assertThat(claimsRefreshToken.getBody().getId()).isEqualTo("foo");
        assertThat(claimsRefreshToken.getBody().getIssuer()).isEqualTo(ISSUER);

    }

    @Test
    public void shouldExtractUserFromUser() throws Exception {
        // execution
        UserTokenDto extractedUser = tokenService.extractUser(signedJwt);

        // assertions
        assertThat(extractedUser).isNotNull();
        assertThat(extractedUser.getId()).isEqualTo(user.getId());
        assertThat(extractedUser.getName()).isEqualTo(user.getName());
        assertThat(extractedUser.getRole()).isEqualByComparingTo(user.getRole());
    }

    @Test
    public void shouldRefreshAccessToken() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, (int) EXPIRE_MONTH);
        Date inOneMonth = cal.getTime();
        Date now = new Date();

        claims.setExpiration(inOneMonth).setId("foo");
        String refreshToken = Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, key)
            .compact();

        String jsonifiedUser = claims.getSubject();

        // execution
        TokensDto tokensHolder = tokenService.refresh(refreshToken);

        // assertions
        assertThat(tokensHolder).isNotNull();

        // access token
        String accessToken = tokensHolder.getAccessToken();
        assertThat(accessToken).isNotBlank();
        Jws<Claims> claimsAccessToken = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(accessToken);

        assertThat(claimsAccessToken.getBody().getIssuedAt()).isCloseTo(now, ACCEPTABLE_OFFSET);
        assertThat(claimsAccessToken.getBody().getExpiration()).isCloseTo(expiresIn, ACCEPTABLE_OFFSET);
        assertThat(claimsAccessToken.getBody().getSubject()).isEqualTo(jsonifiedUser);
        assertThat(claimsAccessToken.getBody().getIssuer()).isEqualTo(ISSUER);

        // refresh token
        assertThat(tokensHolder.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    public void shouldRefreshBothAccessAndRefreshTokens() throws Exception {
        // init
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, (int) EXPIRE_MONTH);
        Date inOneMonth = cal.getTime();
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.HOUR, 23);
        Date closeToInvalidity = cal2.getTime();
        Date now = new Date();

        claims.setExpiration(closeToInvalidity).setId("foo");
        String refreshToken = Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, key)
            .compact();

        String jsonifiedUser = claims.getSubject();

        // execution
        TokensDto tokensHolder = tokenService.refresh(refreshToken);

        // assertions
        assertThat(tokensHolder).isNotNull();

        // access token
        String accessToken = tokensHolder.getAccessToken();
        assertThat(accessToken).isNotBlank();
        Jws<Claims> claimsAccessToken = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(accessToken);

        assertThat(claimsAccessToken.getBody().getIssuedAt()).isCloseTo(now, ACCEPTABLE_OFFSET);
        assertThat(claimsAccessToken.getBody().getExpiration()).isCloseTo(expiresIn, ACCEPTABLE_OFFSET);
        assertThat(claimsAccessToken.getBody().getSubject()).isEqualTo(jsonifiedUser);
        assertThat(claimsAccessToken.getBody().getIssuer()).isEqualTo(ISSUER);

        // refresh token
        String newRefreshToken = tokensHolder.getRefreshToken();
        assertThat(newRefreshToken).isNotBlank();
        Jws<Claims> claimsRefreshToken = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(newRefreshToken);


        assertThat(claimsRefreshToken.getBody().getIssuedAt()).isCloseTo(now, ACCEPTABLE_OFFSET);
        assertThat(claimsRefreshToken.getBody().getExpiration()).isCloseTo(inOneMonth, ACCEPTABLE_OFFSET);
        assertThat(claimsRefreshToken.getBody().getSubject()).isEqualTo(jsonifiedUser);
        assertThat(claimsRefreshToken.getBody().getId()).isEqualTo("foo");
        assertThat(claimsRefreshToken.getBody().getIssuer()).isEqualTo(ISSUER);
    }

    @Test
    public void shouldGetIdentifier() throws Exception {
        // execution
        String jti = tokenService.getIdentifier(signedJwt);

        assertThat(jti).isEqualTo(claims.getId());
    }

    @Test
    public void shouldNotValidateInvalidToken(){
        // init
        signedJwt = Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, generateKey("bar"))
            .compact();

        // execution / assertion
        assertThatThrownBy(() -> {
            tokenService.extractUser(signedJwt);
        }).isInstanceOf(SignatureException.class);
    }

    private static Key generateKey(String secret){
        byte[] encodedSecret = Base64.getEncoder().encode(secret.getBytes());
        return new SecretKeySpec(encodedSecret, SignatureAlgorithm.forName(ALGORITHM).getJcaName());
    }
}