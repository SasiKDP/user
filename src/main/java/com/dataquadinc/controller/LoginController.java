package com.dataquadinc.controller;

import com.dataquadinc.dto.LoginDTO;
import com.dataquadinc.dto.LoginResponseDTO;
import com.dataquadinc.exceptions.InvalidCredentialsException;
import com.dataquadinc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = {"http://35.188.150.92", "http://192.168.0.140:3000", "http://192.168.0.139:3000"})

@RestController
@RequestMapping("/users")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            LoginResponseDTO response = loginService.authenticate(loginDTO);
            return ResponseEntity.ok(response);
        } catch (InvalidCredentialsException e) {
            Map<String, String> errors = new HashMap<>();
            errors.put("300", "Invalid credentials");

            LoginResponseDTO.ErrorDetails errorDetails = new LoginResponseDTO.ErrorDetails(
                    "300",
                    "Invalid credentials"
            );
            LoginResponseDTO errorResponse = new LoginResponseDTO(
                    false,
                    "Unsuccessful",
                    null,
                    errorDetails
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}
