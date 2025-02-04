package com.dataquadinc.repository;

import com.dataquadinc.model.UserDetails_prod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<UserDetails_prod, Long> {
    UserDetails_prod findByEmail(String email);
}
