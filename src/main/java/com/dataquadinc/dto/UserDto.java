package com.dataquadinc.dto;

import com.dataquadinc.model.Roles;
import com.dataquadinc.model.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class UserDto {

    @Id
    @Size( max = 8, message = "User ID must be between 5 and 20 characters")
    private String userId;

    @NotEmpty( message="userName can not be empty")
    @Size(min = 8, max = 20, message = "User name must be between 2 and 50 characters")
    private String userName;



    @NotEmpty(message = "Password must not be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotEmpty

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String confirmPassword;


    @Email
    @Column(unique = true, nullable = false)
    @NotEmpty(message = "Email must not be empty")
    @Size(min = 8, max = 20, message = "Confirm password must be between 8 and 20 characters")
    private String email;

    @Email
    @Column(unique = true, nullable = false)
    @NotEmpty(message = "Email must not be empty")
    @Size(min = 8, max = 20, message = "Confirm password must be between 8 and 20 characters")
    private String personalemail;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phoneNumber;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "gender", nullable = false)
    @Pattern(regexp = "Male|Female", message = "Gender must be Male, Female")
    private String gender;

    @PastOrPresent(message = "Joining date cannot be a future date")
    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }
}