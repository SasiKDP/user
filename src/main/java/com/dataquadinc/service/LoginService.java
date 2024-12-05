package com.dataquadinc.service;
import com.dataquadinc.dto.LoginDTO;
import com.dataquadinc.dto.LoginResponseDTO;
import com.dataquadinc.exceptions.InvalidCredentialsException;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public LoginResponseDTO authenticate(LoginDTO loginDTO) {

        UserDetails userDetails = loginRepository.findByEmail(loginDTO.getEmail());

        if (userDetails == null) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        userDetails.setLastLoginTime(LocalDateTime.now());
        loginRepository.save(userDetails);
        LoginResponseDTO.Payload payload = new LoginResponseDTO.Payload(
                userDetails.getUserId(),
                userDetails.getRoles(),  
                userDetails.getLastLoginTime()
        );
        return new LoginResponseDTO(true, "Login successful", payload);
    }
}
