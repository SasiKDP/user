package com.dataquadinc.dto;

import com.dataquadinc.model.Roles;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

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
        private String userId;
        private Set<Roles> roles;
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
