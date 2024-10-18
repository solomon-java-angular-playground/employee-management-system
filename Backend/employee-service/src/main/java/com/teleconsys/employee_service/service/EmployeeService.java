package com.teleconsys.employee_service.service;

import com.teleconsys.employee_service.dao.EmployeeDao;
import com.teleconsys.employee_service.dto.DepartmentDTO;
import com.teleconsys.employee_service.dto.EmployeeDTO;
import com.teleconsys.employee_service.entity.Employee;
import com.teleconsys.employee_service.feign.DepartmentClient;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Annotazione che indica che la classe EmployeeService è un componente
// gestito da Spring e che contiene la logica di business dell'applicazione
@Service
public class EmployeeService {

    // L'annotazine 'Autowired' dice a Spring di iniettare automaticamente
    // un'istanza di EmployeeDao, che viene usata per interagire con il database
    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentClient departmentClient; // Feign client

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Transactional
    public Employee saveEmployee(Employee employee, String departmentName) {
        // Primo passo: Salva o verifica il dipartimento
        DepartmentDTO department;
        if (departmentName != null) {
            // Prova a salvare il nuovo dipartimento tramite department-service
            department = departmentClient.saveDepartment(new DepartmentDTO(departmentName));
        } else {
            department = departmentClient.getDepartmentById(employee.getEmployeeDepartmentId());
        }

        if (department != null) {
            // Associa il dipartimento salvato all'impiegato
            employee.setEmployeeDepartmentId(department.getDepartmentId());
            employee.setEmployeeDepartmentName(department.getDepartmentName());

            // Secondo passo: Salva l'impiegato
            return employeeDao.save(employee);
        } else {
            throw new RuntimeException("Department creation failed");
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

    // Listener per ascoltare le richieste di impiegati basate su un departmentId inviate dal department-service
    @RabbitListener(queues = "employeeRequestQueue")
    public List<Map<String, Object>> getEmployeesByDepartmentId(Integer departmentId) {
        System.out.println("Received request for departmentId: " + departmentId);
        try {
            List<Employee> employees = employeeDao.findByEmployeeDepartmentId(departmentId);
            System.out.println("Employees fetched: " + employees);

            // Conversione di Employee in Map<String, Object>
            return employees.stream().map(this::convertEmployeeToMap).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving employees by department: " + e.getMessage(), e);
        }
    }

    // Metodo per convertire Employee in Map<String, Object>
    private Map<String, Object> convertEmployeeToMap(Employee employee) {
        return Map.of(
                "employeeId", employee.getEmployeeId(),
                "employeeName", employee.getEmployeeName(),
                "employeeGender", employee.getEmployeeGender(),
                "employeeEmail", employee.getEmployeeEmail(),
                "employeeContactNumber", employee.getEmployeeContactNumber(),
                "employeeAddress", employee.getEmployeeAddress(),
                "employeeSkills", employee.getEmployeeSkills(),
                "employeeDepartmentId", employee.getEmployeeDepartmentId()
        );
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
