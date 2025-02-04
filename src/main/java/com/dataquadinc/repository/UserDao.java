package com.dataquadinc.repository;

import com.dataquadinc.model.UserDetails_prod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import  java.util.*;

@Repository
public interface UserDao extends JpaRepository<UserDetails_prod,Integer> {
    UserDetails_prod findByEmail(String email) ;

    UserDetails_prod findByUserId(String userId);
    UserDetails_prod findByUserName(String userName);

    List<UserDetails_prod> findByRolesId(Long id);
}
