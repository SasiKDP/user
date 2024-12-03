package com.dataquadinc.model;

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
    private UserType name;  //  (e.g., "Admin", "Manager", "Employee")




}
