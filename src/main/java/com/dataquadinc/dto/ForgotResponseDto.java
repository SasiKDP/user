package com.dataquadinc.dto;

public class ForgotResponseDto {
    private boolean success;
    private String message;
    private String error;

    // Constructor for success responses
    public ForgotResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.error = null;  // no error for success responses
    }

    // Constructor for error responses with exception details
    public ForgotResponseDto(boolean success, String message, String error) {
        this.success = success;
        this.message = message;
        this.error = error;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
