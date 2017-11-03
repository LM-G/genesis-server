package com.solofeed.genesis.core.security.auth.service;

import com.solofeed.genesis.core.security.auth.model.JwtUser;
import com.solofeed.genesis.shared.user.dao.UserRepository;
import com.solofeed.genesis.shared.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Security service to handle current connected user loading
 */
@Service
public class UserAuthenticationService implements UserDetailsService {
    @Inject
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // retrieves the user by name
        User user = userRepository.findByName(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        JwtUser userDetails = new JwtUser();
        userDetails.setUser(user);
        return userDetails;
    }
}