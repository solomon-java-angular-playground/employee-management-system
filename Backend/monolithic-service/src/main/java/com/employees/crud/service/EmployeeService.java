package com.employees.crud.service;

import com.employees.crud.dao.EmployeeDao;
import com.employees.crud.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Annotazione che indica che la classe EmployeeService è un componente
// gestito da Spring e che contiene la logica di business dell'applicazione
@Service
public class EmployeeService {

    // L'annotazine 'Autowired' dice a Spring di iniettare automaticamente
    // un'istanza di EmployeeDao, che viene usata per interagire con il database
    @Autowired
    private EmployeeDao employeeDao;

    public Employee saveEmployee(Employee employee) {
        try {
            return employeeDao.save(employee);
        } catch (Exception e) {
            throw new RuntimeException("Error saving employee: " + e.getMessage(), e);
        }
    }

    public List<Employee> getEmployees() {
        try {
            List<Employee> employees = new ArrayList<>();
            employeeDao.findAll().forEach(employees::add);
            return employees;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving employees: " + e.getMessage(), e);
        }
    }

    public Employee getEmployees(Integer employeeId) {
        try {
            return employeeDao.findById(employeeId).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving employee: " + e.getMessage(), e);
        }

    }

    public void deleteEmployee(Integer employeeId) {
        try {
            employeeDao.deleteById(employeeId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting employee: " + e.getMessage(), e);
        }
    }

    public Employee updateEmployee(Employee employee) {
        try {
            employeeDao.findById(employee.getEmployeeId());
            return employeeDao.save(employee);
        } catch (Exception e) {
            throw new RuntimeException("Error updating employee: " + e.getMessage(), e);
        }
    }
}