package com.teleconsys.employee_service.service;

import com.teleconsys.employee_service.entity.EmployeeLog;
import com.teleconsys.employee_service.dao.EmployeeLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeLogService {

    @Autowired
    private EmployeeLogDao employeeLogDao;

    public void logAction(Integer employeeId, String action, String details) {
        EmployeeLog log = new EmployeeLog(employeeId, action, details);
        employeeLogDao.save(log);
    }
}

