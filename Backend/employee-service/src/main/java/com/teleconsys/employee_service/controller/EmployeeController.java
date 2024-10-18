package com.teleconsys.employee_service.controller;

import com.teleconsys.employee_service.dto.UserActivityDTO;
import com.teleconsys.employee_service.feign.DepartmentClient;
import com.teleconsys.employee_service.dto.DepartmentDTO;
import com.teleconsys.employee_service.entity.Employee;
import com.teleconsys.employee_service.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.teleconsys.employee_service.feign.UserActivityClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:4200") // Per accettare richieste provenienti da una specifica origine
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentClient departmentClient;

    @Autowired
    private UserActivityClient userActivityClient;

    @PostMapping
    //@PreAuthorize("hasRole('HR')")
    public Employee saveEmployee(@RequestBody Employee employee, HttpServletRequest request) {
        if (employee.getEmployeeDepartmentId() != null) {
            // Uso del Feign Client per ottenere il dipartimento
            DepartmentDTO departmentDTO = departmentClient.getDepartmentById(employee.getEmployeeDepartmentId());
            if (departmentDTO != null) {
                employee.setEmployeeDepartmentId(departmentDTO.getDepartmentId());
                employee.setEmployeeDepartmentName(departmentDTO.getDepartmentName());
            } else {
                throw new RuntimeException("Department not found");
            }
        } else {
            throw new RuntimeException("Department information is missing");
        }

        Employee savedEmployee = employeeService.saveEmployee(employee, employee.getEmployeeDepartmentName());

        // Traccia l'attività dell'utente e crea l'oggetto UserActivityDTO
        UserActivityDTO userActivity = new UserActivityDTO();
        userActivity.setUserId(request.getUserPrincipal().getName());
        userActivity.setAction("add_employee");
        userActivity.setIpAddress(request.getRemoteAddr());
        userActivity.setDescription("Employee ID: " + savedEmployee.getEmployeeId());
        userActivity.setTimestamp(LocalDateTime.now());

        userActivityClient.trackActivity(userActivity);
        return savedEmployee;
    }

    @GetMapping
    // @PreAuthorize("hasRole('HR')")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/{employeeId}")
    //@PreAuthorize("hasRole('HR')")
    public Employee getEmployee(@PathVariable Integer employeeId) {
        return employeeService.getEmployees(employeeId);
    }

    @GetMapping("/department/{departmentId}")
    public List<Map<String, Object>> getEmployeesByDepartmentId(@PathVariable Integer departmentId) {
        return employeeService.getEmployeesByDepartmentId(departmentId);
    }

    @DeleteMapping("/{employeeId}")
    //@PreAuthorize("hasRole('HR')")
    public void deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
    }

    @PutMapping("/{employeeId}")
    //@PreAuthorize("hasRole('HR')")
    public Employee updateEmployee(@PathVariable Integer employeeId, @RequestBody Employee employee) {
        Employee existingEmployee = employeeService.getEmployees(employeeId);
        if (existingEmployee == null) {
            throw new RuntimeException("Employee not found");
        }

        if (employee.getEmployeeDepartmentId() != null) {
            DepartmentDTO department = departmentClient.getDepartmentById(employee.getEmployeeDepartmentId());
            if (department != null) {
                existingEmployee.setEmployeeDepartmentId(department.getDepartmentId());
                existingEmployee.setEmployeeDepartmentName(department.getDepartmentName());
            } else {
                throw new RuntimeException("Department not found");
            }
        }

        existingEmployee.setEmployeeName(employee.getEmployeeName());
        existingEmployee.setEmployeeEmail(employee.getEmployeeEmail());
        existingEmployee.setEmployeeContactNumber(employee.getEmployeeContactNumber());
        existingEmployee.setEmployeeAddress(employee.getEmployeeAddress());
        existingEmployee.setEmployeeGender(employee.getEmployeeGender());
        existingEmployee.setEmployeeSkills(employee.getEmployeeSkills());

        return employeeService.updateEmployee(existingEmployee);
    }
}
