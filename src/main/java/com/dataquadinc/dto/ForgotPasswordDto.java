package com.dataquadinc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public class ForgotPasswordDto {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email; // User's email

    @NotNull(message = "OTP cannot be null")
    @Size(min = 6, max = 6, message = "OTP must be exactly 6 characters")
    private String otp; // OTP generated for verification

    @NotNull(message = "Update Password cannot be null")
    @Length(min = 8, message = "Password must be at least 8 characters long")
    private String UpdatePassword; // New password to update

    @NotNull(message = "Confirm Password cannot be null")
    @Length(min = 8, message = "Confirm Password must be at least 8 characters long")
    private String ConfirmPassword; // Confirm Password
    // To send success/error messages
    private String message;
    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUpdatePassword() {
        return UpdatePassword;
    }

    public void setUpdatePassword(String updatePassword) {
        UpdatePassword = updatePassword;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
