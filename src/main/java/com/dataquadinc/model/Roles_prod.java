package com.dataquadinc.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Roles_prod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType_prod name;  //  (e.g., "Admin", "Manager", "Employee")

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserType_prod getName() {
        return name;
    }

    public void setName(UserType_prod name) {
        this.name = name;
    }
}
