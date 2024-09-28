package com.employees.crud.dao;

import com.employees.crud.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

// JpaRepository è quasi identico a CrudRepository. Usato per provarlo
public interface UserDao extends CrudRepository<User, Integer> {
    User findByUsername(String username);
}
