package com.dataquadinc.repository;

import com.dataquadinc.model.Roles_prod;
import com.dataquadinc.model.UserType_prod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RolesDao extends JpaRepository<Roles_prod,Integer> {

   Optional<Roles_prod> findByName(UserType_prod name);
}
