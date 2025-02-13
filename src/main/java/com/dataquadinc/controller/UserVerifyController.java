package com.dataquadinc.controller;

import com.dataquadinc.dto.UserVerifyDto;
import com.dataquadinc.service.UserVerifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserVerifyController {

    private final UserVerifyService userService;

    public UserVerifyController(UserVerifyService userService) {
        this.userService = userService;
    }

    // Send OTP to Email
    @PostMapping("/send-otp")
    public ResponseEntity<String>sendOtp(@RequestParam String email) {
        String response = userService.sendOtp(email);
        return ResponseEntity.ok(response);
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody UserVerifyDto userDTO) {
        if (userDTO.getEmail() == null || userDTO.getOtp() == null) {
            return ResponseEntity.badRequest().body("Email and OTP are required");
        }
        String response = userService.verifyOtp(userDTO);
        return ResponseEntity.ok(response);
    }
}

