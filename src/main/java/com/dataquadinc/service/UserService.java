package com.dataquadinc.service;

import com.dataquadinc.dto.UserDto;
import com.dataquadinc.dto.UserResponse;
import com.dataquadinc.exceptions.ValidationException;
import com.dataquadinc.mapper.UserMapper;
import com.dataquadinc.dto.ResponseBean;
import com.dataquadinc.model.Roles;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.repository.RolesDao;
import com.dataquadinc.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RolesDao rolesDao;


    public ResponseEntity<ResponseBean<UserResponse>> registerUser(UserDto userDto) throws ValidationException {


        Map<String,String> errors = new HashMap<>();


        if (userDao.findByUserName(userDto.getUserName()) != null) {

           errors.put("userName","userName already exists");
        }

        if (userDao.findByEmail(userDto.getEmail())!=null) {
            errors.put("email","email is already in use");
        }
        if (userDao.findByUserId(userDto.getUserId())!=null) {
            errors.put("userId","userId already exists");
        }

        if( !errors.isEmpty()) {
            throw new ValidationException(errors);
        }


        // Encrypt the password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setConfirmPassword(passwordEncoder.encode(userDto.getConfirmPassword()));

        // Convert DTO to entity
        UserDetails user = userMapper.toEntity(userDto);

        Set<Roles> roles = userDto.getRoles().stream()
                .map(role -> {
                    try {
                        return rolesDao.findByName(role) // Find Role by its name from RolesDao
                                .orElseThrow(() -> new ValidationException(Map.of("role","roleNotFound")));
                    } catch (ValidationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());

        user.setRoles(roles);


        // Save the user to the database
        UserDetails dbUser = userDao.save(user);
        ResponseBean<UserResponse> resp = new ResponseBean<UserResponse>();
        UserResponse res=new UserResponse();
        res.setUserName(dbUser.getUserName());
        res.setUserId(dbUser.getUserId());
        res.setEmail(dbUser.getEmail());

      resp.setStatus(HttpStatus.CREATED.value());
        resp.setMessage(dbUser.getRoles()+" Created Sucessfully");
        resp.setData(res);

        return new ResponseEntity<ResponseBean<UserResponse>>(resp,HttpStatus.CREATED);



    }


    public ResponseEntity<Set<Roles>> getRolesByUserId( String UserId ) {
        UserDetails user = userDao.findByUserId(UserId);
        Set<Roles> roles = user.getRoles();
        return  ResponseEntity.ok(roles);



    }

}
