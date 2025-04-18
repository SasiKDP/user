package com.dataquadinc.service;

import com.dataquadinc.dto.LogoutResponseDTO;
import com.dataquadinc.dto.Payload;
import com.dataquadinc.exceptions.UserNotFoundException;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.repository.UserDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class LogoutService {

    private final UserDao userDao;

    public LogoutService(UserDao userDao) {
        this.userDao = userDao;
    }

    public LogoutResponseDTO logout(String userId) {
        // Throws exception if user doesn't exist
        UserDetails userDetails = userDao.findByUserId(userId);
        if (userDetails == null) {
            throw new UserNotFoundException(userId);
        }

        // Proceed with logout
        userDetails.setLastLoginTime(null); // End session
        userDao.save(userDetails);
        resetUserSession(userDetails); // Clear other session state if needed

        Payload payload = new Payload(userId, LocalDateTime.now());

        return new LogoutResponseDTO(true, "Logout successful", payload, null);
    }

    private void resetUserSession(UserDetails userDetails) {
        // If needed, clear additional session fields
        userDetails.setLastLoginTime(null);
        userDao.save(userDetails);
    }
}