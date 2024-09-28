package com.teleconsys.employee_service.controller;

import com.employees.crud.entity.Department;
import com.employees.crud.entity.Employee;
import com.employees.crud.service.DepartmentService;
import com.employees.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    // POST
    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public Employee saveEmployee(@RequestBody Employee employee) {
        if (employee.getEmployeeDepartmentId() != null) {
            // Trova il dipartimento usando l'ID
            Department department = departmentService.getDepartment(employee.getEmployeeDepartmentId());
            if (department != null) {
                employee.setEmployeeDepartment(department);
            } else {
                // Gestisce il caso in cui il dipartimento non esiste
                throw new RuntimeException("Department not found");
            }
        } else {
            // Gestisce il caso in cui l'ID del dipartimento non è stato fornito
            throw new RuntimeException("Department information is missing");
        }
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    @PreAuthorize("hasRole('HR')")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasRole('HR')")
    public Employee getEmployee(@PathVariable Integer employeeId) {
        return employeeService.getEmployees(employeeId);
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('HR')")
    public void deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
    }

    @PutMapping("/{employeeId}")
    @PreAuthorize("hasRole('HR')")
    public Employee updateEmployee(@PathVariable Integer employeeId, @RequestBody Employee employee) {
        Employee existingEmployee = employeeService.getEmployees(employeeId);
        // Trova il dipartimento usando l'ID
        if (existingEmployee == null) {
            throw new RuntimeException("Employee not found");
        }

        // Aggiorna il dipartimento se fornito
        if (employee.getEmployeeDepartment() != null) {
            Department department = departmentService.getDepartment(employee.getEmployeeDepartment().getDepartmentId());
            existingEmployee.setEmployeeDepartment(department);
        }

        existingEmployee.setEmployeeName(employee.getEmployeeName());
        existingEmployee.setEmployeeEmail(employee.getEmployeeEmail());
        existingEmployee.setEmployeeContactNumber(employee.getEmployeeContactNumber());
        existingEmployee.setEmployeeAddress(employee.getEmployeeAddress());
        existingEmployee.setEmployeeDepartmentId(employee.getEmployeeDepartmentId());
        existingEmployee.setEmployeeGender(employee.getEmployeeGender());
        existingEmployee.setEmployeeSkills(employee.getEmployeeSkills());

        if (employee.getEmployeeDepartmentId() != null) {
            // Trova il dipartimento usando l'ID
            Department department = departmentService.getDepartment(employee.getEmployeeDepartmentId());
            if (department != null) {
                existingEmployee.setEmployeeDepartment(department);
            } else {
                // Gestisce il caso in cui il dipartimento non esiste
                throw new RuntimeException("Department not found");
            }
        } else {
            // Gestisce il caso in cui l'ID del dipartimento non è stato fornito
            throw new RuntimeException("Department information is missing");
        }

        return employeeService.updateEmployee(existingEmployee);
    }
}

