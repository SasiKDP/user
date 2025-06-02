package com.dataquadinc.controller;

import com.dataquadinc.dto.LoginDTO;
import com.dataquadinc.dto.LoginResponseDTO;
import com.dataquadinc.exceptions.InvalidCredentialsException;
import com.dataquadinc.exceptions.UserAlreadyLoggedInException;
import com.dataquadinc.exceptions.UserNotFoundException;
import com.dataquadinc.exceptions.UserInactiveException; // Import the UserInactiveException
import com.dataquadinc.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

            // Removed cookie logic

            System.out.println("=== CONTROLLER: Returning 200 OK response ===");
            return ResponseEntity.ok(response);

        } catch (UserAlreadyLoggedInException e) {
            System.out.println("=== CONTROLLER: Caught UserAlreadyLoggedInException: " + e.getMessage() + " ===");
            System.out.println("=== CONTROLLER: Re-throwing to global handler ===");
            throw e;
        } catch (UserNotFoundException e) {
            System.out.println("=== CONTROLLER: Caught UserNotFoundException: " + e.getMessage() + " ===");
            throw e;
        } catch (InvalidCredentialsException e) {
            System.out.println("=== CONTROLLER: Caught InvalidCredentialsException: " + e.getMessage() + " ===");
            throw e;
        } catch (Exception e) {
            System.out.println("=== CONTROLLER: Caught unexpected exception: " + e.getClass().getName() + " - " + e.getMessage() + " ===");
            e.printStackTrace();
            throw e;
        }
    }
}
