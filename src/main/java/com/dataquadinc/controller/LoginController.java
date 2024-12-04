package com.dataquadinc.controller;

import com.dataquadinc.dto.LoginDTO;
import com.dataquadinc.dto.LoginResponseDTO;
import com.dataquadinc.exceptions.InvalidCredentialsException;
import com.dataquadinc.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            LoginResponseDTO response = loginService.authenticate(loginDTO);
            return ResponseEntity.ok(response);
        } catch (InvalidCredentialsException e) {

            LoginResponseDTO response = new LoginResponseDTO(
                    false,
                    "Invalid credentials",
                    null
            );


            response.setErrors(Map.of("ERROR", "Invalid credentials"));

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
