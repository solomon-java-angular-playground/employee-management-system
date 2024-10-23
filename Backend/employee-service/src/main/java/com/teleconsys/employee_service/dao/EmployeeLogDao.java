package com.teleconsys.employee_service.dao;

import com.teleconsys.employee_service.entity.EmployeeLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeLogDao extends MongoRepository<EmployeeLog, String> {
}
