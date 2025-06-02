package com.dataquadinc.service;

import com.dataquadinc.repository.UserDao;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user by email since username is the email
        com.dataquadinc.model.UserDetails user = userDao.findByEmail(email); // Find user by email instead of username
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return User.builder()
                .username(user.getEmail())  // Use email as the username
                .password(user.getPassword())
                .roles(String.valueOf(user.getRoles()))
                .build();
    }
}