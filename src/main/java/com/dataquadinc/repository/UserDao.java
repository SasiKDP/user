package com.dataquadinc.repository;

import com.dataquadinc.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  java.util.*;

@Repository
public interface UserDao extends JpaRepository<UserDetails,Integer> {
    UserDetails findByEmail(String email) ;

    UserDetails findByUserId( String userId);
    UserDetails findByUserName(String userName);

    List<UserDetails> findByRolesId(Long id);
}
