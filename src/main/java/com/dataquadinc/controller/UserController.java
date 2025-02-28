package com.dataquadinc.controller;

import com.dataquadinc.dto.EmployeeWithRole;
import com.dataquadinc.dto.UserDto;
import com.dataquadinc.dto.ResponseBean;
import com.dataquadinc.dto.UserResponse;
import com.dataquadinc.exceptions.UserNotFoundException;
import com.dataquadinc.model.Roles;

import com.dataquadinc.model.UserDetails;
import com.dataquadinc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


//@CrossOrigin(origins = "http://35.188.150.92")
////@CrossOrigin(origins = "http://192.168.0.139:3000")
////git status



@CrossOrigin(origins = {"http://35.188.150.92", "http://192.168.0.140:3000", "http://192.168.0.139:3000","https://mymulya.com"})
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseBean<UserResponse>> registerUser(@Valid  @RequestBody UserDto userDto) throws RoleNotFoundException {

         return   userService.registerUser(userDto);

    }
    @GetMapping("/{userId}/email")
    public ResponseEntity<String> getRecruiterEmail(@PathVariable String userId) {
        try {
            // Fetch the recruiter details using the userId
            UserDetails recruiter = userService.getRecruiterById(userId);

            // Log the recruiter object
            System.out.println("Fetched recruiter: " + recruiter);

            if (recruiter == null) {
                throw new UserNotFoundException("Recruiter not found with ID: " + userId);
            }

            // Log the email being returned
            System.out.println("Email: " + recruiter.getEmail());

            // Return the email as a plain response
            return ResponseEntity.ok(recruiter.getEmail());

        } catch (UserNotFoundException ex) {
            // Log the error
            System.out.println("Error: " + ex.getMessage());

            // Return an error response with a suitable status code and message
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + ex.getMessage());
        }
    }

    @GetMapping("/{userIds}/username")
    public ResponseEntity<String> getRecruiterUsername(@PathVariable String userIds) {
        try {
            // Split the userIds string into individual IDs
            String[] idArray = userIds.split(",");
            StringBuilder result = new StringBuilder();
            List<String> notFoundIds = new ArrayList<>();

            // Process each ID individually
            for (String userId : idArray) {
                try {
                    UserDetails recruiter = userService.getRecruiterById(userId.trim());

                    if (recruiter != null) {
                        // If not first entry, add a comma
                        if (result.length() > 0) {
                            result.append(",");
                        }
                        result.append(recruiter.getUserName());
                    } else {
                        notFoundIds.add(userId.trim());
                    }
                } catch (UserNotFoundException ex) {
                    notFoundIds.add(userId.trim());
                }
            }

            // If we found no valid users
            if (result.length() == 0) {
                throw new UserNotFoundException("Recruiters not found with IDs: " + String.join(",", notFoundIds));
            }

            // Log the final result
            System.out.println("Usernames found: " + result.toString());
            if (!notFoundIds.isEmpty()) {
                System.out.println("IDs not found: " + String.join(",", notFoundIds));
            }

            return ResponseEntity.ok(result.toString());

        } catch (UserNotFoundException ex) {
            // Log the error
            System.out.println("Error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + ex.getMessage());
        } catch (Exception ex) {
            // Log any unexpected errors
            System.out.println("Unexpected error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: An unexpected error occurred");
        }
    }


    @PostMapping("/addusers")
    public ResponseEntity<ResponseBean<UserResponse>> registerUsers(@Valid  @RequestBody UserDto userDto) throws RoleNotFoundException {

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
