package com.dataquadinc.dto;

import lombok.Data;

import java.util.Map;

@Data
public class    UserResponse {
    private String userId;
    private String userName;
    private String Email;
     // To indicate success
    private String error;
}
