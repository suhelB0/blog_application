package com.blogapplication.BlogApplication.security;

import com.blogapplication.BlogApplication.Entity.User;
import com.blogapplication.BlogApplication.repository.UserRepository;

import com.blogapplication.BlogApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getUserByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("error");
        }

        return new CustomUserDetails(user);
    }
}
