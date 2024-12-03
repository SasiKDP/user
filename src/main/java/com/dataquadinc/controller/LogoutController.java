package com.dataquadinc.controller;
import com.dataquadinc.dto.LogoutResponse;
import com.dataquadinc.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/logout")
@CrossOrigin("*")
public class LogoutController {
    @Autowired
    private LogoutService logoutService;

    @PutMapping("/save/{userId}")
    public ResponseEntity<LogoutResponse> saveUser(@PathVariable String userId) {
        LogoutResponse response = logoutService.logoutUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
