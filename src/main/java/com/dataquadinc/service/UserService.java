
package com.dataquadinc.service;

import com.dataquadinc.dto.EmployeeWithRole;
import com.dataquadinc.dto.UserDto;
import com.dataquadinc.dto.UserResponse;
import com.dataquadinc.exceptions.ValidationException;
import com.dataquadinc.mapper.UserMapper;
import com.dataquadinc.dto.ResponseBean;
import com.dataquadinc.model.Roles;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.model.UserType;
import com.dataquadinc.repository.RolesDao;
import com.dataquadinc.repository.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RolesDao rolesDao;

//    public ResponseEntity<ResponseBean<UserResponse>> registerUser(UserDto userDto) throws ValidationException {
//
//        Map<String,String> errors = new HashMap<>();
//
//
////        if (userDao.findByUserName(userDto.getUserName()) != null) {
////
////            errors.put("userName","userName already exists");
////        }
//
//        if (userDao.findByEmail(userDto.getEmail())!=null) {
//            errors.put("errormessage","email is already in use");
//        }
//        if (userDao.findByUserId(userDto.getUserId())!=null) {
//            errors.put("errorMessage","userId already exists");
//        }
//
//        if( !errors.isEmpty()) {
//            throw new ValidationException(errors);
//        }
//
//
//        // Encrypt the password
//        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
//        userDto.setConfirmPassword(passwordEncoder.encode(userDto.getConfirmPassword()));
//
//        // Convert DTO to entity
//        UserDetails user = userMapper.toEntity(userDto);
//
//        Set<Roles> roles = userDto.getRoles().stream()
//                .map(role -> {
//                    try {
//                        return rolesDao.findByName(role) // Find Role by its name from RolesDao
//                                .orElseThrow(() -> new ValidationException(Map.of("role","roleNotFound")));
//                    } catch (ValidationException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .collect(Collectors.toSet());
//
//        user.setRoles(roles);
//
//        // Save the user to the database
//        UserDetails dbUser = userDao.save(user);
//
//        UserResponse res=new UserResponse();
//        res.setUserName(dbUser.getUserName());
//        res.setUserId(dbUser.getUserId());
//        res.setEmail(dbUser.getEmail());
//        // Set success to true
//
//
//        ResponseBean<UserResponse> resp = new ResponseBean<UserResponse>();
//        resp.setSuccess(true);
//        resp.setMessage(" Created Sucessfully");
//        resp.setData(res);
//        resp.setError(null);
//
//        return new ResponseEntity<ResponseBean<UserResponse>>(resp,HttpStatus.CREATED);
//
//    }

    public ResponseEntity<ResponseBean<UserResponse>> registerUser(UserDto userDto) throws ValidationException {
        Map<String, String> errors = new HashMap<>();

        // Check if email or userId already exists
        if (userDao.findByEmail(userDto.getEmail()) != null) {
            errors.put("errormessage", "email is already in use");
        }
        if (userDao.findByUserId(userDto.getUserId()) != null) {
            errors.put("errorMessage", "userId already exists");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        // Encrypt the password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setConfirmPassword(passwordEncoder.encode(userDto.getConfirmPassword()));

        // Convert DTO to entity
        UserDetails user = userMapper.toEntity(userDto);

        // Map roles to the user
        Set<Roles> roles = userDto.getRoles().stream()
                .map(role -> {
                    try {
                        return rolesDao.findByName(role) // Find Role by its name from RolesDao
                                .orElseThrow(() -> new ValidationException(Map.of("role", "roleNotFound")));
                    } catch (ValidationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());

        user.setRoles(roles);

        // Save the user to the database
        UserDetails dbUser = userDao.save(user);

        // Create a response object
        UserResponse res = new UserResponse();
        res.setUserName(dbUser.getUserName());
        res.setUserId(dbUser.getUserId());
        res.setEmail(dbUser.getEmail());

        // Create and send registration success email
        sendRegistrationConfirmationEmail(dbUser.getEmail());

        // Prepare the response bean
        ResponseBean<UserResponse> resp = new ResponseBean<>();
        resp.setSuccess(true);
        resp.setMessage("Created Successfully");
        resp.setData(res);
        resp.setError(null);

        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    // Method to send the registration confirmation email
    private void sendRegistrationConfirmationEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Registration Successful");
        message.setText("Your registration has been successfully completed. Welcome to our platform!");

        try {
            mailSender.send(message);
            System.out.println("Registration confirmation email sent to " + email);
        } catch (Exception e) {
            System.out.println("Error sending registration confirmation email to " + email);
            e.printStackTrace();
            throw new RuntimeException("Error sending registration confirmation email: " + e.getMessage());
        }
    }




    public ResponseEntity<Set<Roles>> getRolesByUserId( String UserId ) {
        UserDetails user = userDao.findByUserId(UserId);
        Set<Roles> roles = user.getRoles();
        return  ResponseEntity.ok(roles);

    }

//    public List<EmployeeWithRole> getRolesId(long id) {
//        List<UserDetails> list = userDao.findByRolesId(id);
//
//        if (list.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        return list.stream()
//                .map(e -> new EmployeeWithRole(e.getUserId(), e.getUserName(),e.getRoles()))
//                .collect(Collectors.toList());
//    }
//
//    public ResponseEntity<List<EmployeeWithRole>> getAllEmployeesWithRoles() {
//        List<UserDetails> users = userDao.findAll();
//        if (users.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        List<EmployeeWithRole> employeeRoles = users.stream()
//                .map(user -> new EmployeeWithRole(
//                        user.getUserId(),
//                        user.getUserName(),
//                        user.getRoles()
//                ))
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(employeeRoles, HttpStatus.OK);
//    }


    public ResponseEntity<List<EmployeeWithRole>> getAllEmployeesWithRoles() {
        // Fetch all users from the database
        List<UserDetails> users = userDao.findAll();

        // If no users are found, return a No Content response
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Map each UserDetails to an EmployeeWithRole, flattening the roles into a single string
        List<EmployeeWithRole> employeeRoles = users.stream()
                .map(user -> {
                    // Ensure that user.getRoles() returns a valid Set<Roles> and handle empty roles correctly
                    String roleName = Optional.ofNullable(user.getRoles())  // Null check for user roles
                            .flatMap(roles -> roles.stream()
                                    .map(role -> role.getName().name())  // Access enum name as String
                                    .findFirst())  // Get the first role name if present
                            .orElse("No Role");  // Default to "No Role" if no roles are found or empty

                    // Create and return the EmployeeWithRole object
                    return new EmployeeWithRole(
                            user.getUserId(),       // Set user ID
                            user.getUserName(),
                            // Set user name
                            roleName  ,
                            user.getEmail()// Set role name as a simple string
                    );
                })
                .collect(Collectors.toList());

        // Return the list of EmployeeWithRole objects with an OK response status
        return new ResponseEntity<>(employeeRoles, HttpStatus.OK);
    }




}


















