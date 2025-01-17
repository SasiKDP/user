package com.dataquadinc.dto;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class Payload extends LogoutResponseDTO.Payload {
    private String userId;
    private LocalDateTime logoutTimestamp;

    // Constructor with parameters
    public Payload(String userId, LocalDateTime logoutTimestamp) {
        this.userId = userId;
        this.logoutTimestamp = logoutTimestamp;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getLogoutTimestamp() {
        return logoutTimestamp;
    }

    public void setLogoutTimestamp(LocalDateTime logoutTimestamp) {
        this.logoutTimestamp = logoutTimestamp;
    }
}
