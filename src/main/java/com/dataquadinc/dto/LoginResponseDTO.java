package com.dataquadinc.dto;

import com.dataquadinc.model.UserType;
import lombok.*;

import java.time.LocalDateTime;

@Data

@NoArgsConstructor
public class LoginResponseDTO {

    private boolean success;
    private String message;
    private Payload payload;
    private ErrorDetails error;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payload {
        private String userId;
        private UserType roleType;
        private LocalDateTime loginTimestamp;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorDetails {
        private String errorCode;
        private String errorMessage;
    }


    public LoginResponseDTO(boolean success, String message, Payload payload) {
        this.success = success;
        this.message = message;
        this.payload = payload;
        this.error = null;
    }

    public LoginResponseDTO(boolean success, String message, Payload payload, ErrorDetails error) {
        this.success = success;
        this.message = message;
        this.payload = payload;
        this.error = error;
    }
}
