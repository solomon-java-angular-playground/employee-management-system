package com.teleconsys.department_service.dao;

import com.employees.crud.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentDao extends JpaRepository<Department, Integer>, QuerydslPredicateExecutor<Department> {
    /*@Query(value = "SELECT d.department_id, d.department_name, e.employee_id, e.employee_name, e.employee_contact_number, e.employee_address, e.employee_gender, e.employee_skills " +
            "FROM Department d LEFT JOIN Employee e ON d.department_id = e.department_id " +
            "ORDER BY d.department_name", nativeQuery = true)
    List<Object[]> getDepartmentsWithEmployees();*/
}

