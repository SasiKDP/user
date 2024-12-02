package com.dataquadinc.service;

import com.dataquadinc.exceptions.DuplicateEmailException;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String register(UserDetails userDetails) {

        if (loginRepository.findByEmail(userDetails.getEmail()) != null) {
            throw new DuplicateEmailException("Email is already in use");
        }
        if (userDetails.getRole() == null || userDetails.getRole().isEmpty()) {
            userDetails.setRole("USER");
        }
        userDetails.setLoginTimestamp(LocalDateTime.now());

        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        loginRepository.save(userDetails);

        return "Registration successful";
    }
}
