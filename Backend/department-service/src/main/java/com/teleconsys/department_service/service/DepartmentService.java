package com.teleconsys.department_service.service;

import com.teleconsys.department_service.dao.DepartmentDao;
import com.teleconsys.department_service.entity.Department;
import com.teleconsys.department_service.dto.EmployeeDTO;
import com.teleconsys.department_service.feign.EmployeeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private EmployeeClient employeeClient; // Inject Feign client

    public Department saveDepartment(Department department) {
        try {
            return departmentDao.save(department);
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

    // Metodo per ottenere i dipartimenti con gli impiegati utilizzando EmployeeDTO
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

    public Department updateDepartment(Department department) {
        try {
            return departmentDao.save(department);
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
