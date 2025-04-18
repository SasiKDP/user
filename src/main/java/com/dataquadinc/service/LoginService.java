
package com.dataquadinc.service;

import com.dataquadinc.dto.LoginDTO;
import com.dataquadinc.dto.LoginResponseDTO;
import com.dataquadinc.exceptions.InvalidCredentialsException;
import com.dataquadinc.exceptions.UserAlreadyLoggedInException;
import com.dataquadinc.exceptions.UserInactiveException;
import com.dataquadinc.exceptions.UserNotFoundException;  // Import the new exception
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.model.UserType;
import com.dataquadinc.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public LoginResponseDTO authenticate(LoginDTO loginDTO) {

        UserDetails userDetails = loginRepository.findByEmail(loginDTO.getEmail());

        if (userDetails == null) {
            throw new UserNotFoundException("User not found with email: " + loginDTO.getEmail());
        }

        if (userDetails.getStatus() == null || !userDetails.getStatus().equalsIgnoreCase("ACTIVE")) {
            throw new UserInactiveException("User is inactive and cannot log in.");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        if (userDetails.getRoles() == null || userDetails.getRoles().isEmpty()) {
            throw new InvalidCredentialsException("No roles assigned to the user");
        }

        // Optional: Enable this if you want to prevent login while session is active
//        Duration sessionTimeout = Duration.ofMinutes(30);
//        if (userDetails.getLastLoginTime() != null) {
//            Duration timeSinceLastLogin = Duration.between(userDetails.getLastLoginTime(), LocalDateTime.now());
//            if (timeSinceLastLogin.compareTo(sessionTimeout) < 0) {
//                throw new UserAlreadyLoggedInException("User is already logged in and session is active.");
//            }
//        }

        // Proceed with login
        userDetails.setLastLoginTime(LocalDateTime.now());
        loginRepository.save(userDetails);

        UserType roleType = userDetails.getRoles().iterator().next().getName();

        LoginResponseDTO.Payload payload = new LoginResponseDTO.Payload(
                userDetails.getUserId(),
                userDetails.getUserName(),
                userDetails.getEmail(),
                roleType,
                userDetails.getLastLoginTime()
        );

        return new LoginResponseDTO(true, "Login successful", payload);
    }
}
