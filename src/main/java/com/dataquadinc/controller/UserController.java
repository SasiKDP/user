package com.dataquadinc.controller;

import com.dataquadinc.dto.UserDto;
import com.dataquadinc.dto.ResponseBean;
import com.dataquadinc.dto.UserResponse;
import com.dataquadinc.model.Roles;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseBean<UserResponse>> registerUser(@RequestBody UserDto userDto) throws RoleNotFoundException {

         return   userService.registerUser(userDto);

    }
//    @GetMapping("/roles/{userId}")
//    public ResponseEntity<Set<Roles>> getRolesByUserId(@PathVariable String userId ) {
//        return userService.getRolesByUserId(userId);
//    }

    @GetMapping("/roles")
    public String getRolesByUserId( ) {
        return "hello world";
    }

}
