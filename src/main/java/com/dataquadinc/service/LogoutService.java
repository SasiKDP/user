package com.dataquadinc.service;

import com.dataquadinc.dto.LogoutResponseDTO;
import com.dataquadinc.model.Roles;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.repository.UserDao;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class LogoutService {

    private final UserDao userDao;

    public LogoutService(UserDao userDao) {
        this.userDao = userDao;
    }

    public LogoutResponseDTO logout(String userId) {
        boolean success = processLogout(userId);

        if (success) {

            LocalDateTime logoutTimestamp = LocalDateTime.now();
            LogoutResponseDTO.Payload payload = new LogoutResponseDTO.Payload(userId,logoutTimestamp);
            return new LogoutResponseDTO(true, "Logout successful", payload);
        } else {

           Map<String, String> errors = new HashMap<>();
            errors.put("error", "Logout failed for user: " + userId);
            return new LogoutResponseDTO(false, "Logout failed", null, errors);
        }
    }

    private boolean processLogout(String userId) {
        UserDetails userDetails = userDao.findByUserId(userId);
        return userDetails != null;
    }

}

