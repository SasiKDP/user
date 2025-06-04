package com.dataquadinc.controller;

import com.dataquadinc.dto.LoginDTO;
import com.dataquadinc.dto.LoginResponseDTO;
import com.dataquadinc.exceptions.InvalidCredentialsException;
import com.dataquadinc.exceptions.UserAlreadyLoggedInException;
import com.dataquadinc.exceptions.UserNotFoundException;
import com.dataquadinc.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO,
                                                  HttpServletRequest request,
                                                  HttpServletResponse httpResponse) {
        System.out.println("=== CONTROLLER: Starting login process ===");

        try {
            LoginResponseDTO response = loginService.authenticate(loginDTO, request);

            System.out.println("=== CONTROLLER: Service returned successfully ===");
            System.out.println("=== CONTROLLER: Response message: " + response.getMessage() + " ===");

            HttpHeaders headers = new HttpHeaders();

            if (response.getPayload() != null && response.getPayload().getToken() != null) {
                String token = response.getPayload().getToken();

                // Set secure, HTTP-only cookie — Gateway will convert this into Authorization header
                ResponseCookie cookie = ResponseCookie.from("authToken", token)
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(24 * 60 * 60) // 24 hours
                        .sameSite("Lax")
                        .build();

                headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
                System.out.println("=== CONTROLLER: Set authToken cookie for Gateway ===");
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(response);

        } catch (UserAlreadyLoggedInException | UserNotFoundException | InvalidCredentialsException e) {
            System.out.println("=== CONTROLLER: Known exception: " + e.getMessage() + " ===");
            throw e; // Let global exception handler manage it
        } catch (Exception e) {
            System.out.println("=== CONTROLLER: Unexpected exception: " + e.getClass().getName() + " - " + e.getMessage() + " ===");
            e.printStackTrace();
            throw e;
        }
    }
}
