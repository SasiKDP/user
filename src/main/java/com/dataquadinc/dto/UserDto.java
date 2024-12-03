package com.dataquadinc.dto;

import com.dataquadinc.model.Roles;
import com.dataquadinc.model.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDto {

    @Id
    private String userId;

    @NotEmpty
    private String userName;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;

    @Email
    @NotEmpty
    private String email;

    @Email
    private String personalemail;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotEmpty
    private String designation;


    private Set<UserType> roles; // Linked to Role entity



// Getters and setters


    public Set<UserType> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserType> roles) {
        this.roles = roles;
    }

    public @NotEmpty String getUserId() {
        return userId;
    }

    public void setUserId(@NotEmpty String userId) {
        this.userId = userId;
    }

    public @NotEmpty String getUserName() {
        return userName;
    }

    public void setUserName(@NotEmpty String userName) {
        this.userName = userName;
    }

    public @NotEmpty String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty String password) {
        this.password = password;
    }

    public @Email @NotEmpty String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotEmpty String email) {
        this.email = email;
    }

    public @Email String getPersonalemail() {
        return personalemail;
    }

    public void setPersonalemail(@Email String personalemail) {
        this.personalemail = personalemail;
    }

    public @NotEmpty @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotEmpty @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotEmpty String getDesignation() {
        return designation;
    }

    public void setDesignation(@NotEmpty String designation) {
        this.designation = designation;
    }

    public @NotEmpty String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotEmpty String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
