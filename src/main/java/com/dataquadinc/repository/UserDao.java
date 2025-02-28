package com.dataquadinc.repository;

import com.dataquadinc.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import  java.util.*;

@Repository
public interface UserDao extends JpaRepository<UserDetails,Integer> {
    UserDetails findByEmail(String email) ;

    UserDetails findByUserId(String userId);
    UserDetails findByUserName(String userName);

    List<UserDetails> findByRolesId(Long id);
    @Query("SELECT u FROM UserDetails u WHERE u.personalemail = :personalemail")
    UserDetails findByPersonalEmail(String personalemail);
    ;
}
