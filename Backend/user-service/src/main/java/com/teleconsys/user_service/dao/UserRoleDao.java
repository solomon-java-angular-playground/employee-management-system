package com.teleconsys.user_service.dao;

import com.employees.crud.entity.User;
import com.employees.crud.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleDao extends JpaRepository<UserRole, Integer> {
    List<UserRole> findByUser(User user);
}

