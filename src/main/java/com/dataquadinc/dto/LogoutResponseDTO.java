package com.dataquadinc.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
public class LogoutResponseDTO {

    private boolean success;
    private String message;
    private Payload payload;
    private Map<String, String> errors;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payload {
        private String userId;
        private LocalDateTime logoutTimestamp;
    }

    // Custom constructor
    public LogoutResponseDTO(boolean success, String message, Payload payload, Map<String, String> errors) {
        this.success = success;
        this.message = message;
        this.payload = payload;
        this.errors = errors;
    }
}
