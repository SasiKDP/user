package com.dataquadinc.service;

import com.dataquadinc.dto.LoginDTO;
import com.dataquadinc.dto.LoginResponseDTO;
import com.dataquadinc.exceptions.InvalidCredentialsException;
import com.dataquadinc.exceptions.UserAlreadyLoggedInException;
import com.dataquadinc.exceptions.UserInactiveException;
import com.dataquadinc.exceptions.UserNotFoundException;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.model.UserType;
import com.dataquadinc.repository.LoginRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public LoginResponseDTO authenticate(LoginDTO loginDTO, HttpServletRequest request) {
        System.out.println("=== SERVICE: Starting authentication ===");

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

        // Check if already logged in via valid JWT token from cookie
        String existingToken = extractTokenFromCookie(request);
        if (existingToken != null) {
            System.out.println("Token found in cookie: " + existingToken);
            try {
                boolean isValid = jwtService.validateToken(existingToken, loginDTO.getEmail());
                System.out.println("=== SERVICE: Token validation result: " + isValid + " ===");

                if (isValid) {
                    throw new UserAlreadyLoggedInException("User already logged in");
                }
            } catch (UserAlreadyLoggedInException e) {
                throw e;
            } catch (Exception e) {
                System.out.println("Token validation failed: " + e.getMessage());
            }
        } else {
            System.out.println("No token found in cookies.");
        }

        // Proceed with login
        userDetails.setLastLoginTime(LocalDateTime.now());
        loginRepository.save(userDetails);

        // Generate new JWT token
        String token = jwtService.generateToken(userDetails.getEmail());

        // Return response
        return buildResponse(userDetails, token, "Login successful");
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println("Found cookie: " + cookie.getName() + " = " + cookie.getValue());
                if ("authToken".equals(cookie.getName())) {
                    System.out.println("Token extracted from cookie");
                    return cookie.getValue();
                }
            }
        } else {
            System.out.println("No cookies found in request.");
        }
        return null;
    }

    private LoginResponseDTO buildResponse(UserDetails userDetails, String token, String message) {
        UserType roleType = userDetails.getRoles().iterator().next().getName();

        // Encode encryption key using Base64
        String encodedKey = Base64.getEncoder().encodeToString(userDetails.getEncryptionKey().getBytes());

        LoginResponseDTO.Payload payload = new LoginResponseDTO.Payload(
                userDetails.getUserId(),
                userDetails.getUserName(),
                userDetails.getEmail(),
                roleType,
                userDetails.getLastLoginTime(),
                token,
                encodedKey,
                userDetails.getEntity()
        );

        // Assuming LoginResponseDTO has an overloaded constructor to also include token
        return new LoginResponseDTO(true, message, payload);
    }
}
