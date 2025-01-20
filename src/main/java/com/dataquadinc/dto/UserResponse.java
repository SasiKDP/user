package com.dataquadinc.dto;

import lombok.Data;

import java.util.Map;

@Data
public class    UserResponse {
    private String userId;
    private String userName;
    private String Email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
