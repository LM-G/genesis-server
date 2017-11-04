package com.solofeed.genesis.core.security.config;

import com.solofeed.genesis.core.config.ApplicationConfig;
import com.solofeed.genesis.core.security.RestAuthenticationEntryPoint;
import com.solofeed.genesis.core.security.jwt.JwtAuthenticationFilter;
import com.solofeed.genesis.core.security.jwt.JwtAuthenticationProvider;
import com.solofeed.genesis.util.SkipPathRequestMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * Application security provider.
 */
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private static final String BASE_URL = "/" + ApplicationConfig.APPLICATION_PATH;
    private static final String AUTH_ENTRY_POINT = BASE_URL + "/auth/";
    private static final String SIGN_IN_ENTRY_POINT = AUTH_ENTRY_POINT + "sign-in";
    private static final String SIGN_UP_ENTRY_POINT = AUTH_ENTRY_POINT + "sign-up";
    private static final String PROTECTED_ENTRY_POINT = BASE_URL + "/**";

    @Inject
    private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Inject
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Inject
    private AuthenticationManager authenticationManager;
    @Inject
    private SecurityProps securityProps;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Creates a new instance of {@link BCryptPasswordEncoder}
     * @return the new password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Sets the CORS basic configuration, authorizes any source
     * @return CORS configuration
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // we don't need CSRF because our token is invulnerable
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.authorizeRequests()
                // we always authorize login and registration action
                .antMatchers(HttpMethod.POST, SIGN_IN_ENTRY_POINT).permitAll()
                .antMatchers(HttpMethod.POST, SIGN_UP_ENTRY_POINT).permitAll()
                .and()
                // but other requests are protected
                .authorizeRequests().antMatchers(PROTECTED_ENTRY_POINT).authenticated()
                .and()
                // custom JWT filter
                .addFilterBefore(buildJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // disables Spring session creation
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // no page caching
                .headers().cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    private JwtAuthenticationFilter buildJwtAuthenticationFilter()  {
        List<String> pathsToSkip = Arrays.asList(SIGN_IN_ENTRY_POINT, SIGN_UP_ENTRY_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, PROTECTED_ENTRY_POINT);
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(securityProps, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }
}
