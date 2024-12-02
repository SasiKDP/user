package com.dataquadinc.model;

import com.dataquadinc.model.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class UserDetails {

    @Id
    @Column(unique = true)
    private String userId; // This is set manually from the frontend

    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable=false)
    private String confirmPassword;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Email
    @Column(unique = true, nullable = false)
    private String personalemail;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotEmpty
    private String designation;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt= LocalDateTime.now();;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt=LocalDateTime.now();;


    @Column(name = "last_Login_Time")
    private LocalDateTime lastLoginTime;



    @Column(nullable = false)
    @ManyToMany
    @JoinTable(
            name = "user_roles", // Name of the join table
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key to UserDetails
            inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key to Roles
    )
    private Set<Roles> roles = new HashSet<>();


   
}
