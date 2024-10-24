package com.teleconsys.employee_service.service;

import com.teleconsys.employee_service.dao.EmployeeDao;
import com.teleconsys.employee_service.dto.DepartmentDTO;
import com.teleconsys.employee_service.entity.Employee;
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
    private RabbitTemplate rabbitTemplate;  // RabbitTemplate per inviare messaggi

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    // Annotazione per rendere la transazione transazionale/atomica, quindi reversibile in caso di errore
    @Transactional
    @SuppressWarnings("unchecked")
    public Employee saveEmployee(Employee employee, String departmentName) {
        // Crea la richiesta come una semplice mappa
        Map<String, Object> departmentRequest = Map.of("departmentName", departmentName);

        // Invia il messaggio e ricevi la risposta come mappa
        Map<String, Object> departmentMap = (Map<String, Object>) rabbitTemplate.convertSendAndReceive(
                "departmentExchange",
                "department.create",
                departmentRequest
        );

        if (departmentMap != null) {
            // Estrazione dei dati dalla mappa
            Integer departmentId = (Integer) departmentMap.get("departmentId");
            String departmentNameResponse = (String) departmentMap.get("departmentName");

            // Associa il dipartimento all'impiegato
            employee.setEmployeeDepartmentId(departmentId);
            employee.setEmployeeDepartmentName(departmentNameResponse);

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

    @Transactional
    public void deleteEmployee(Integer employeeId) {
        try {
            employeeDao.deleteById(employeeId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting employee: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Employee updateEmployee(Employee employee) {
        try {
            employeeDao.findById(employee.getEmployeeId());
            return employeeDao.save(employee);
        } catch (Exception e) {
            throw new RuntimeException("Error updating employee: " + e.getMessage(), e);
        }
    }

    // Metodo per convertire una Map in DepartmentDTO
    private DepartmentDTO convertMapToDepartmentDTO(Map<String, Object> departmentMap) {
        Integer id = (Integer) departmentMap.get("departmentId");
        String name = (String) departmentMap.get("departmentName");
        return new DepartmentDTO(id, name);
    }
}
