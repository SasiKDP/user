package com.dataquadinc.controller;
import com.dataquadinc.dto.LogoutResponseDTO;
import com.dataquadinc.exceptions.UserNotFoundException;
import com.dataquadinc.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(origins = {
        "http://35.188.150.92",
        "http://192.168.0.140:3000",
        "http://192.168.0.139:3000",
        "https://mymulya.com",
        "http://localhost:3000",
        "http://192.168.0.135:8080", // Sixth IP
        "http://182.18.177.16:443", // Seventh IP
        "http://192.168.0.135:80", // Eighth IP
        "http://localhost/", // Ninth IP
        "http://mymulya.com:443" // Tenth IP
})

@RestController
@RequestMapping("/users")
public class  LogoutController {

    private final LogoutService logoutService;

    @Autowired
    public LogoutController(LogoutService logoutService) {
        this.logoutService = logoutService;
    }

    @PutMapping("/logout/{userId}")
    public ResponseEntity<LogoutResponseDTO> logout(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(logoutService.logout(userId));
        } catch (UserNotFoundException e) {
            Map<String, String> error = Map.of("userId", e.getMessage());
            LogoutResponseDTO response = new LogoutResponseDTO(false, "Logout failed", null, error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}


