package com.dataquadinc.service;

import com.dataquadinc.dto.LoginDTO;
import com.dataquadinc.dto.LoginResponseDTO;
import com.dataquadinc.exceptions.InvalidCredentialsException;
import com.dataquadinc.exceptions.UserAlreadyLoggedInException;
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
        // Check if user exists in the database
        UserDetails userDetails = loginRepository.findByEmail(loginDTO.getEmail());

        if (userDetails == null) {
            // If user is not found, throw a UserNotFoundException
            throw new UserNotFoundException("User not found with email: " + loginDTO.getEmail());
        }

        // Validate password
        if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        // Check if roles are present before accessing them
        if (userDetails.getRoles() == null || userDetails.getRoles().isEmpty()) {
            throw new InvalidCredentialsException("No roles assigned to the user");
        }

        // Define the session timeout duration (e.g., 30 minutes)
        Duration sessionTimeout = Duration.ofMinutes(30);

        // Check if the user is logged in based on last login time and session timeout
        if (userDetails.getLastLoginTime() != null) {
            // If last login time is within the timeout, treat user as logged in
            Duration timeSinceLastLogin = Duration.between(userDetails.getLastLoginTime(), LocalDateTime.now());
            if (timeSinceLastLogin.compareTo(sessionTimeout) < 0) {
                throw new UserAlreadyLoggedInException("User is already logged in.");
            }
        }

        // If user is not already logged in (or session expired), proceed to login
        // Update the last login time
        userDetails.setLastLoginTime(LocalDateTime.now());
        loginRepository.save(userDetails);

        // Get the first role assigned to the user
        UserType roleType = userDetails.getRoles().iterator().next().getName();

        // Create payload
        LoginResponseDTO.Payload payload = new LoginResponseDTO.Payload(
                userDetails.getUserId(),
                roleType,
                userDetails.getLastLoginTime()
        );

        // Return successful login response
        return new LoginResponseDTO(true, "Login successful", payload);
    }
}
