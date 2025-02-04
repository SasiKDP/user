package com.dataquadinc.service;

import com.dataquadinc.dto.LoginDTO;
import com.dataquadinc.dto.LoginResponseDTO;
import com.dataquadinc.exceptions.InvalidCredentialsException;
import com.dataquadinc.model.UserDetails_prod;
import com.dataquadinc.model.UserType_prod;
import com.dataquadinc.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public LoginResponseDTO authenticate(LoginDTO loginDTO) {
        UserDetails_prod userDetails = loginRepository.findByEmail(loginDTO.getEmail());

        if (userDetails == null || !passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        // Check if roles are present before accessing them
        if (userDetails.getRoles() == null || userDetails.getRoles().isEmpty()) {
            throw new InvalidCredentialsException("No roles assigned to the user");
        }



        userDetails.setLastLoginTime(LocalDateTime.now());
        loginRepository.save(userDetails);
        UserType_prod roleType = userDetails.getRoles().iterator().next().getName();

        LoginResponseDTO.Payload payload = new LoginResponseDTO.Payload(
//                userDetails.getUserId(),
//                userDetails.getRoles().iterator().next().getName(),
//                userDetails.getLastLoginTime()
                userDetails.getUserId(),
                roleType,
                userDetails.getLastLoginTime()
        );


        return new LoginResponseDTO(true, "Login successful", payload);
    }
}