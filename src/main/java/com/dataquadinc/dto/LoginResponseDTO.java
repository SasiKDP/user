package com.dataquadinc.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginResponseDTO {

    private boolean success;
    private String message;
    private Payload payload;
    private Map<String, String> errors;  // Added errors map for failure cases

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payload {
        private Long userId;
        private String role;
        private LocalDateTime loginTimestamp;
    }

    // Custom constructor to include message and errors
    public LoginResponseDTO(boolean success, String message, Payload payload) {
        this.success = success;
        this.message = message;
        this.payload = payload;
    }

    // Setter for errors
    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
