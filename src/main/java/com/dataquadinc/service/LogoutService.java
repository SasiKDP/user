package com.dataquadinc.service;

import com.dataquadinc.dto.LogoutResponseDTO;
import com.dataquadinc.dto.Payload;
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
        boolean success = processLogout(userId);

        if (success) {
            // Get the current timestamp for logout
            LocalDateTime logoutTimestamp = LocalDateTime.now();

            // Optionally, you can reset other session-related fields if needed
            resetUserSession(userId);

            // Build the payload for response
            Payload payload = new Payload(userId, logoutTimestamp);

            // Return successful logout response
            return new LogoutResponseDTO(true, "Logout successful", payload, null);
        } else {
            // If logout failed, return failure response with an error
            Map<String, String> errors = new HashMap<>();
            errors.put("error", "Logout failed for user: " + userId);
            return new LogoutResponseDTO(false, "Logout failed", null, errors);
        }
    }

    private boolean processLogout(String userId) {
        // Find the user details from the database using user ID
        UserDetails userDetails = userDao.findByUserId(userId);

        // Check if the user exists
        if (userDetails != null) {
            // If the user exists, proceed with logout logic
            // Example: You might want to clear session-related fields like `lastLoginTime`
            userDetails.setLastLoginTime(null); // Reset the last login time to simulate session end
            userDao.save(userDetails); // Save the updated user details to the database

            // Optionally, you can also clear session tokens or authentication mechanisms here (if used)
            // For example, you might want to invalidate a session token or JWT.

            return true; // Successful logout
        } else {
            return false; // User not found, logout failed
        }
    }

    // Method to reset user session state (clear any session-related fields)
    private void resetUserSession(String userId) {
        UserDetails userDetails = userDao.findByUserId(userId);

        if (userDetails != null) {
            // Reset session-related fields (like lastLoginTime, session token, etc.)
            userDetails.setLastLoginTime(null); // Clear the last login time
            // If you're using JWT or any session tokens, clear those as well
            // userDetails.setSessionToken(null); // Example: Reset session token (if you are using it)
            userDao.save(userDetails); // Save the updated user details to persist changes
        }
    }
}
