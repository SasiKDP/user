package com.dataquadinc.service;

import com.dataquadinc.dto.LoginDTO;
import com.dataquadinc.dto.LoginResponseDTO;
import com.dataquadinc.exceptions.InvalidCredentialsException;
import com.dataquadinc.exceptions.UserAlreadyLoggedInException;
import com.dataquadinc.exceptions.UserNotFoundException;  // Import the new exception
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
        // Check if user exists in the database
        UserDetails_prod userDetails = loginRepository.findByEmail(loginDTO.getEmail());

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

        // Check if the user is already logged in (you can use a flag like 'isLoggedIn')
        if (userDetails.getLastLoginTime() != null) {
            throw new UserAlreadyLoggedInException("User is already logged in.");
        }

        // Update the last login time
        userDetails.setLastLoginTime(LocalDateTime.now());
        loginRepository.save(userDetails);

        // Get the first role assigned to the user
        UserType_prod roleType = userDetails.getRoles().iterator().next().getName();

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
