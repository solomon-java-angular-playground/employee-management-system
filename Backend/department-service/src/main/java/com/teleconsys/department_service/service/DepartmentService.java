package com.teleconsys.department_service.service;

import com.teleconsys.department_service.dao.DepartmentDao;
import com.teleconsys.department_service.dto.DepartmentDTO;
import com.teleconsys.department_service.entity.Department;
import com.teleconsys.department_service.dto.EmployeeDTO;
import com.teleconsys.department_service.feign.EmployeeClient;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;  // RabbitTemplate per inviare messaggi

    @Transactional  // Assicura che il salvataggio e l'invio del messaggio siano atomici
    @RabbitListener(queues = "departmentRequestQueue")
    public Map<String, Object> saveDepartment(Map<String, Object> departmentRequest) {
        // Log per vedere cosa arriva
        System.out.println("Received department request: " + departmentRequest);
        try {
            // Estrai il nome del dipartimento dalla richiesta
            String departmentName = (String) departmentRequest.get("departmentName");

            // Controlla se esiste un dipartimento con lo stesso nome
            Department existingDepartment = departmentDao.findByDepartmentName(departmentName);
            if (existingDepartment != null) {
                // Restituisci una mappa con i dettagli del dipartimento esistente
                return convertDepartmentToMap(existingDepartment);
            }

            // Se non esiste, crea un nuovo dipartimento
            Department newDepartment = new Department();
            newDepartment.setDepartmentName(departmentName);
            Department savedDepartment = departmentDao.save(newDepartment);

            // Restituisci una mappa con i dettagli del dipartimento appena creato
            return convertDepartmentToMap(savedDepartment);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save department: " + e.getMessage());
        }
    }

    @Transactional
    public DepartmentDTO saveDepartment(DepartmentDTO departmentDto) {
        // Controlla se il dipartimento esiste
        Department existingDepartment = departmentDao.findByDepartmentName(departmentDto.getDepartmentName());
        if (existingDepartment != null) {
            return convertToDTO(existingDepartment);  // Restituisci il dipartimento esistente
        }

        // Se non esiste, crea un nuovo dipartimento
        Department newDepartment = new Department();
        newDepartment.setDepartmentName(departmentDto.getDepartmentName());
        Department savedDepartment = departmentDao.save(newDepartment);

        return convertToDTO(savedDepartment);
    }

    // Metodo di utilità per convertire un Department in una mappa
    private Map<String, Object> convertDepartmentToMap(Department department) {
        return Map.of(
                "departmentId", department.getDepartmentId(),
                "departmentName", department.getDepartmentName()
        );
    }

    // Metodo di utilità per convertire un Department in DepartmentDTO
    private DepartmentDTO convertToDTO(Department department) {
        return new DepartmentDTO(department.getDepartmentId(), department.getDepartmentName());
    }


    public List<DepartmentDTO> getDepartments() {
        try {
            List<Department> departments = new ArrayList<>();
            departmentDao.findAll().forEach(departments::add);
            return departments.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Unable to get departments: " + e.getMessage());
        }
    }

    public DepartmentDTO getDepartment(Integer departmentId) {
        Department department = departmentDao.findById(departmentId).orElseThrow(() ->
                new RuntimeException("Department not found"));
        return convertToDTO(department);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDepartmentsWithEmployees() {
        List<Department> departments = departmentDao.findAll();
        List<Map<String, Object>> departmentsWithEmployees = new ArrayList<>();

        for (Department department : departments) {
            List<Map<String, Object>> employees = (List<Map<String, Object>>) rabbitTemplate.convertSendAndReceive(
                    "employeeExchange",  // Nome dell'exchange su RabbitMQ
                    "employee.routingkey",  // Routing key usata per questa richiesta
                    department.getDepartmentId()  // ID del dipartimento da inviare nella richiesta
            );

            if (employees != null) {
                for (Map<String, Object> employeeData : employees) {
                    Map<String, Object> departmentEmployeeData = new HashMap<>();
                    departmentEmployeeData.put("departmentId", department.getDepartmentId());
                    departmentEmployeeData.put("departmentName", department.getDepartmentName());
                    departmentEmployeeData.put("employee", employeeData);
                    departmentsWithEmployees.add(departmentEmployeeData);
                }
            }
        }
        return departmentsWithEmployees;
    }

    @Transactional
    public DepartmentDTO updateDepartment(DepartmentDTO departmentDto) {
        Department department = departmentDao.findById(departmentDto.getDepartmentId()).orElseThrow(() ->
                new RuntimeException("Department not found"));

        department.setDepartmentName(departmentDto.getDepartmentName());
        Department updatedDepartment = departmentDao.save(department);

        return convertToDTO(updatedDepartment);
    }

    @Transactional
    public void deleteDepartment(Integer departmentId) {
        try {
            departmentDao.deleteById(departmentId);
        } catch (Exception e) {
            throw new RuntimeException("Unable to delete department: " + e.getMessage());
        }
    }
}
