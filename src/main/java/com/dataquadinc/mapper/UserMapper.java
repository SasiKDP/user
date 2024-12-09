package com.dataquadinc.mapper;

import com.dataquadinc.dto.UserDto;
import com.dataquadinc.exceptions.ValidationException;
import com.dataquadinc.model.Roles;
import com.dataquadinc.model.UserDetails;
import com.dataquadinc.model.UserType;
import com.dataquadinc.repository.RolesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.relation.RoleNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private RolesDao roleDao;  // You will need to create this DAO

    public UserDetails toEntity(UserDto userDto) throws ValidationException {
        UserDetails user = new UserDetails();
        user.setUserId(userDto.getUserId());
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());// Password should be encrypted later
        user.setConfirmPassword(userDto.getConfirmPassword());
        user.setEmail(userDto.getEmail());
        user.setPersonalemail(userDto.getPersonalemail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setDesignation(userDto.getDesignation());
        user.setDob(userDto.getDob());
        user.setGender(userDto.getGender());
       user.setJoiningDate(userDto.getJoiningDate());


        return user;
    }
}
