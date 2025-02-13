package com.dataquadinc.dto;



public class UserVerifyDto {
    private String email;
    private String otp;

    public UserVerifyDto() {}

    public UserVerifyDto(String email, String otp) {
        this.email = email != null ? email.trim() : null;
        this.otp = otp != null ? otp.trim() : null;
    }

    // ... existing getters and setters ...


    public String getEmail() {
        return email;
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
}
