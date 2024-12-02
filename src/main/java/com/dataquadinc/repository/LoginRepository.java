package com.dataquadinc.repository;

import com.dataquadinc.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<UserDetails, Long> {
    UserDetails findByEmail(String email);
}
