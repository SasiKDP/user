package com.dataquadinc.controller;

import com.dataquadinc.dto.EmployeeWithRole;
import com.dataquadinc.dto.UserDto;
import com.dataquadinc.dto.ResponseBean;
import com.dataquadinc.dto.UserResponse;
import com.dataquadinc.model.Roles;

import com.dataquadinc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

import java.util.List;
import java.util.Set;

<<<<<<< Updated upstream
=======
//@CrossOrigin(origins = "http://35.188.150.92")
////@CrossOrigin(origins = "http://192.168.0.139:3000")
////git status

>>>>>>> Stashed changes

@CrossOrigin(origins = {"http://35.188.150.92", "http://192.168.0.140:3000", "http://192.168.0.139:3000"})
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseBean<UserResponse>> registerUser(@Valid  @RequestBody UserDto userDto) throws RoleNotFoundException {

         return   userService.registerUser(userDto);

    }
    @GetMapping("/roles/{userId}")
    public ResponseEntity<Set<Roles>> getRolesByUserId(@PathVariable String userId ) {
        return userService.getRolesByUserId(userId);
    }

    @GetMapping("/employee")
    public ResponseEntity<List<EmployeeWithRole>> getAllEmployees() {
        ResponseEntity<List<EmployeeWithRole>> responseEntity = userService.getAllEmployeesWithRoles();
        List<EmployeeWithRole> employeeRoles = responseEntity.getBody();
        if (employeeRoles == null || employeeRoles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(employeeRoles, HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ResponseBean<UserResponse>> updateUser(@PathVariable String userId, @Valid @RequestBody UserDto userDto) throws RoleNotFoundException {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ResponseBean<UserResponse>> deleteUser(@PathVariable String userId) {

        return userService.deleteUser(userId);

    }

}
