package com.teleconsys.user_service.dao;

import com.teleconsys.user_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {

    // Metodo per trovare un ruolo per nome
    Optional<Role> findByName(String name);
}


