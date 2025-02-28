package com.dataquadinc.repository;

import com.dataquadinc.model.Roles;
import com.dataquadinc.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RolesDao extends JpaRepository<Roles,Integer> {

   Optional<Roles> findByName(UserType name);
}
