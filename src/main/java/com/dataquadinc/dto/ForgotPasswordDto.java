package com.dataquadinc.dto;

public class ForgotPasswordDto {
    private String email; // User's email
    private String otp; // OTP generated for verification
    private String UpdatePassword; // New password to update
    private String ConfirmPassword;
    private String message; // To send success/error messages

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
