package com.teleconsys.department_service.dao;

import com.teleconsys.department_service.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentDao extends JpaRepository<Department, Integer> {
    Department findByDepartmentName(String departmentName);
}

