package com.dataquadinc.controller;

import com.dataquadinc.dto.ForgotPasswordDto;
import com.dataquadinc.service.ForgotPasswordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    // Forgot Password (Generate OTP)
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        String email = forgotPasswordDto.getEmail();

        // Validate email
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email cannot be null or empty");
        }

        // Generate OTP
        try {
            String otp = forgotPasswordService.generateOtp(email);
            forgotPasswordDto.setOtp(otp);  // Store the OTP in the DTO
            return ResponseEntity.ok("OTP sent successfully. Please check your email.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating OTP: " + e.getMessage());
        }
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        String email = forgotPasswordDto.getEmail();
        String otp = forgotPasswordDto.getOtp();

        if (email == null || otp == null || email.isEmpty() || otp.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email and OTP are required");
        }

        // Verify OTP
        boolean isOtpValid = forgotPasswordService.verifyOtp(email, otp);
        if (isOtpValid) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP. Please try again.");
        }
    }

    // Update Password
    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        String email = forgotPasswordDto.getEmail();
        String updatePassword = forgotPasswordDto.getUpdatePassword();
        String confirmPassword = forgotPasswordDto.getConfirmPassword();

        // Validate the fields (check if they are not null or empty)
        if (email == null || email.isEmpty() || updatePassword == null || confirmPassword == null || updatePassword.isEmpty() || confirmPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email and Update Password and Confirm Password cannot be null or empty");
        }

        // Check if the password and confirm password match
        if (!updatePassword.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password and Confirm Password do not match.");
        }

        // Update the password
        try {
            forgotPasswordService.updatePassword(email,updatePassword);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating password: " + e.getMessage());
        }
    }
}
