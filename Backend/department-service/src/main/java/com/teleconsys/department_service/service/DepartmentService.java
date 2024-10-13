package com.teleconsys.department_service.service;

import com.teleconsys.department_service.dao.DepartmentDao;
import com.teleconsys.department_service.entity.Department;
import com.teleconsys.department_service.dto.EmployeeDTO;
import com.teleconsys.department_service.feign.EmployeeClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;  // RabbitTemplate per inviare messaggi

    /* Feign non più necessario perchè usiamo
    @Autowired
    private EmployeeClient employeeClient; // Inject Feign client
     */

    public Department saveDepartment(Department department) {
        try {
            Department savedDepartment = departmentDao.save(department);

            // Pubblica un messaggio su RabbitMQ
            rabbitTemplate.convertAndSend("departmentExchange", "department.routingkey", savedDepartment);

            return savedDepartment;
        } catch (Exception e) {
            throw new RuntimeException("Unable to save department: " + e.getMessage());
        }
    }

    public List<Department> getDepartments() {
        try {
            List<Department> departments = new ArrayList<>();
            departmentDao.findAll().forEach(departments::add);
            return departments;
        } catch (Exception e) {
            throw new RuntimeException("Unable to get departments: " + e.getMessage());
        }
    }

    public Department getDepartment(Integer departmentId) {
        try {
            return departmentDao.findById(departmentId).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("Unable to get department: " + e.getMessage());
        }
    }

    public List<Object[]> getDepartmentsWithEmployees() {
        List<Department> departments = departmentDao.findAll();
        List<Object[]> departmentsWithEmployees = new ArrayList<>();

        for (Department department : departments) {
            // Invia una richiesta per ottenere gli impiegati di un dipartimento
            List<EmployeeDTO> employees = (List<EmployeeDTO>) rabbitTemplate.convertSendAndReceive(
                    "employeeExchange",  // Nome dell'exchange su RabbitMQ
                    "employee.routingkey",  // Routing key usata per questa richiesta
                    department.getDepartmentId()  // ID del dipartimento da inviare nella richiesta
            );

            // Costruisce la lista di dipartimenti con i loro dipendenti
            if (employees != null) {
                for (EmployeeDTO employeeDTO : employees) {
                    Object[] departmentEmployeeData = new Object[]{
                            department.getDepartmentId(),
                            department.getDepartmentName(),
                            employeeDTO
                    };
                    departmentsWithEmployees.add(departmentEmployeeData);
                }
            }
        }
        return departmentsWithEmployees;
    }

    /* //Metodo per ottenere i dipartimenti con gli impiegati utilizzando EmployeeDTO
    public List<Object[]> getDepartmentsWithEmployees() {
        List<Department> departments = departmentDao.findAll();

        List<Object[]> departmentsWithEmployees = new ArrayList<>();
        for (Department department : departments) {
            List<EmployeeDTO> employees = employeeClient.getEmployeesByDepartmentId(department.getDepartmentId());

            for (EmployeeDTO employeeDTO : employees) {
                Object[] departmentEmployeeData = new Object[]{
                        department.getDepartmentId(),
                        department.getDepartmentName(),
                        employeeDTO
                };
                departmentsWithEmployees.add(departmentEmployeeData);
            }
        }

        return departmentsWithEmployees;
    }
    */

    public Department updateDepartment(Department department) {
        try {
            Department updatedDepartment = departmentDao.save(department);

            // Pubblica un messaggio su RabbitMQ per notificare l'aggiornamento del dipartimento
            rabbitTemplate.convertAndSend("departmentExchange", "department.routingkey", updatedDepartment);

            return updatedDepartment;
        } catch (Exception e) {
            throw new RuntimeException("Unable to update department: " + e.getMessage());
        }
    }

    public void deleteDepartment(Integer departmentId) {
        try {
            departmentDao.deleteById(departmentId);
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete department: " + e.getMessage());
        }
    }
}
