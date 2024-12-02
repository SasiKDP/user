package com.dataquadinc.controller;

import com.dataquadinc.exceptions.DuplicateEmailException;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDetails userDetails) {
        try {
            String result = userService.register(userDetails);

            return ResponseEntity.ok().body(result);

        } catch (DuplicateEmailException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
