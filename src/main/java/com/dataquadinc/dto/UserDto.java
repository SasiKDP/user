package com.dataquadinc.dto;


import com.dataquadinc.model.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
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
        @Size(min = 20, max = 50, message = "email must be between 20 and 50 characters")
        private String email;

        @Email
        @Column(unique = true, nullable = false)
        @NotEmpty(message = "Email must not be empty")
        @Size(min = 20, max = 50, message = "email must be between 20 and 50 characters")
        private String personalemail;

        @NotEmpty
        @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
        private String phoneNumber;

        @Column(name = "dob", nullable = false)
    //    @Past(message = "Date of birth must be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private String dob;

        @Column(name = "gender", nullable = false)
        @Pattern(regexp = "Male|Female", message = "Gender must be Male, Female")
        private String gender;

    //    @Past(message = "Date of birth must be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @Column(name = "joining_date", nullable = false)
        @NotNull
        private LocalDate joiningDate;


        @NotEmpty
        private String designation;


        private Set<UserType> roles;

    public @Size(max = 8, message = "User ID must be between 5 and 20 characters") String getUserId() {
        return userId;
    }

    public void setUserId(@Size(max = 8, message = "User ID must be between 5 and 20 characters") String userId) {
        this.userId = userId;
    }

    public @NotEmpty(message = "userName can not be empty") @Size(min = 8, max = 20, message = "User name must be between 2 and 50 characters") String getUserName() {
        return userName;
    }

    public void setUserName(@NotEmpty(message = "userName can not be empty") @Size(min = 8, max = 20, message = "User name must be between 2 and 50 characters") String userName) {
        this.userName = userName;
    }

    public @NotEmpty(message = "Password must not be empty") @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "Password must not be empty") @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters") String password) {
        this.password = password;
    }

    public @NotEmpty @NotEmpty(message = "Password must not be empty") @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters") String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotEmpty @NotEmpty(message = "Password must not be empty") @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters") String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public @Email @NotEmpty(message = "Email must not be empty") @Size(min = 20, max = 50, message = "email must be between 20 and 50 characters") String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotEmpty(message = "Email must not be empty") @Size(min = 20, max = 50, message = "email must be between 20 and 50 characters") String email) {
        this.email = email;
    }

    public @Email @NotEmpty(message = "Email must not be empty") @Size(min = 20, max = 50, message = "email must be between 20 and 50 characters") String getPersonalemail() {
        return personalemail;
    }

    public void setPersonalemail(@Email @NotEmpty(message = "Email must not be empty") @Size(min = 20, max = 50, message = "email must be between 20 and 50 characters") String personalemail) {
        this.personalemail = personalemail;
    }

    public @NotEmpty @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotEmpty @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public @NotNull LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(@NotNull LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public @Pattern(regexp = "Male|Female", message = "Gender must be Male, Female") String getGender() {
        return gender;
    }

    public void setGender(@Pattern(regexp = "Male|Female", message = "Gender must be Male, Female") String gender) {
        this.gender = gender;
    }


    public @NotEmpty String getDesignation() {

        return designation;

    }

    public void setDesignation(@NotEmpty String designation) {
        this.designation = designation;
    }

    public Set<UserType> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserType> roles) {
        this.roles = roles;
    }
}