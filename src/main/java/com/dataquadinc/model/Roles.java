package com.dataquadinc.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Data
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType name;  //

    // (e.g., "Admin", "Manager", "Employee")

    @ManyToMany(mappedBy = "roles")
    private Set<UserDetails> userDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserType getName() {
        return name;
    }

    public void setName(UserType name) {
        this.name = name;
    }

    public Set<UserDetails> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(Set<UserDetails> userDetails) {
        this.userDetails = userDetails;
    }
}
