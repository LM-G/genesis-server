package com.solofeed.genesis.core.config.security;

import com.solofeed.genesis.core.config.ApplicationConfig;
import com.solofeed.genesis.core.security.filter.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.inject.Inject;

/**
 * Application security provider.
 */
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    /** Auth end-point in which user doesn't need to be authenticated */
    public static final String AUTH_URL = "/" + ApplicationConfig.APPLICATION_PATH + "/auth/";

    @Inject private UserDetailsService userDetailsService;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * Describes the Spring authentication provider behavior with the custom {@link UserDetailsService} implementation.
     * @return authentication provider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
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
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    /**
     * Beanify the {@link JWTAuthenticationFilter} filter
     * @return jwt authent filter
     */
    @Bean
    public JWTAuthenticationFilter authenticationFilter() {
        return new JWTAuthenticationFilter();
    }


    /**
     * Security Properties
     * @return Security properties bean
     */
    @Bean
    public SecurityProps securityProps(){
        return new SecurityProps();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // we don't need CSRF because our token is invulnerable
        http.cors().and().csrf().disable();

        http.authorizeRequests()
                // we always authorize login and registration action
                .antMatchers(HttpMethod.POST, AUTH_URL + "sign-in").permitAll()
                .antMatchers(HttpMethod.POST, AUTH_URL + "sign-out").permitAll()
                // but other requests are protec
                .anyRequest().authenticated()
                .and()
                // custom JWT filter
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // disables Spring session creation
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // no page caching
                .headers().cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}
