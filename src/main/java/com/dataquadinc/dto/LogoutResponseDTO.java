package com.dataquadinc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogoutResponseDTO {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("message")
    private String message;
    @JsonProperty("payload")
    private Payload payload;
    @JsonProperty("errors")
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
