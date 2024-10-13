package com.teleconsys.employee_service.service;

import com.teleconsys.employee_service.dao.EmployeeDao;
import com.teleconsys.employee_service.dto.EmployeeDTO;
import com.teleconsys.employee_service.entity.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Annotazione che indica che la classe EmployeeService è un componente
// gestito da Spring e che contiene la logica di business dell'applicazione
@Service
public class EmployeeService {

    // L'annotazine 'Autowired' dice a Spring di iniettare automaticamente
    // un'istanza di EmployeeDao, che viene usata per interagire con il database
    @Autowired
    private EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

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

    // Listener per ascoltare le richieste di impiegati basate su un departmentId inviate dal department-service
    @RabbitListener(queues = "employeeRequestQueue")  // Ascolta le richieste nella coda RabbitMQ
    public List<EmployeeDTO> getEmployeesByDepartmentId(Integer departmentId) {
        try {
            List<Employee> employees = employeeDao.findByEmployeeDepartmentId(departmentId);

            // Converti l'elenco di Employee in EmployeeDTO
            return employees.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving employees by department: " + e.getMessage(), e);
        }
    }

    // Metodo per convertire Employee in EmployeeDTO
    private EmployeeDTO convertToDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getEmployeeId(),
                employee.getEmployeeName(),
                employee.getEmployeeSkills(),
                employee.getEmployeeEmail(),   // Aggiunto per trasferire l'email
                employee.getEmployeeDepartmentId()
        );
    }

    /*
    public List<Employee> getEmployeesByDepartmentId(Integer departmentId) {
        try {
            return employeeDao.findByEmployeeDepartmentId(departmentId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving employees by department: " + e.getMessage(), e);
        }
    }
    */

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
