package com.dataquadinc.dto;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LogoutResponse
{
    private String userId;
    private LocalDateTime logoutTime;

    public LocalDateTime getLogoutTime() {

        return logoutTime;
    }

    public void setLogoutTime(LocalDateTime logoutTime) {

        this.logoutTime = logoutTime;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {

        this.userId = userId;
    }
}

