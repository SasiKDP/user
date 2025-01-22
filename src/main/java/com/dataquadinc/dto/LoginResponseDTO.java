package com.dataquadinc.dto;

import com.dataquadinc.model.UserType;  // Correct import
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null values during serialization
public class LoginResponseDTO {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("message")
    private String message;
    @JsonProperty("payload")
    private Payload payload;
    @JsonProperty("error")
    private ErrorDetails error;

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

    @Data
    @NoArgsConstructor

    @JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null values during serialization
    public static class Payload {
        @JsonProperty("userId") // Explicitly map this field
        private String userId;

        @JsonProperty("roleType") // Explicitly map this field
        private UserType roleType;

        @JsonProperty("loginTimestamp") // Explicitly map this field
        private LocalDateTime loginTimestamp;

        public Payload(String userId, UserType roleType, LocalDateTime loginTimestamp) {
            this.userId = userId;
            this.roleType = roleType;
            this.loginTimestamp = loginTimestamp;
        }
    }

    @Data
    @NoArgsConstructor

    @JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null values during serialization
    public static class ErrorDetails {
        @JsonProperty("errorCode") // Explicitly map this field
        private String errorCode;

        @JsonProperty("errorMessage") // Explicitly map this field
        private String errorMessage;

        // Add manual constructor
        public ErrorDetails(String errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

    }

}
