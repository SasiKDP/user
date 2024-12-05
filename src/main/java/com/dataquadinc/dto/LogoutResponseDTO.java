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
    public class LogoutResponseDTO {

        private boolean success;
        private String message;
        private com.dataquadinc.dto.LogoutResponseDTO.Payload payload;
        private Map<String, String> errors;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Payload {
            private String userId;
            private LocalDateTime logoutTimestamp;
        }

        public LogoutResponseDTO(boolean success, String message, com.dataquadinc.dto.LogoutResponseDTO.Payload payload) {
            this.success = success;
            this.message = message;
            this.payload = payload;
        }
        public void setErrors(Map<String, String> errors) {
            this.errors = errors;
        }
    }

