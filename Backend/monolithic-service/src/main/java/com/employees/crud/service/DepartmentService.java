package com.employees.crud.service;

import com.employees.crud.dao.DepartmentDao;
import com.employees.crud.entity.Department;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.employees.crud.entity.QEmployee;
import com.employees.crud.entity.QDepartment;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;

    // Per costruire le query con QueryDSL
    private final JPAQueryFactory queryFactory;

    @Autowired
    public DepartmentService(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

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

    // Metodo per ottenere i dipartimenti con gli impiegati
    public List<Object[]> getDepartmentsWithEmployees() {
        QDepartment department = QDepartment.department;
        QEmployee employee = QEmployee.employee;

        // Esecuzione della la query e
        // ottenimento del risultato come lista di Tuple
        List<Tuple> results = queryFactory
                .select(department.departmentId, department.departmentName, employee.employeeId, employee.employeeName,
                        employee.employeeContactNumber, employee.employeeAddress, employee.employeeGender, employee.employeeSkills, employee.employeeEmail)
                .from(department)
                .leftJoin(department.employees, employee)
                .orderBy(department.departmentName.asc())
                .fetch();


        // Conversione della lista di Tuple in una lista di Object[]
        List<Object[]> departmentsWithEmployees = new ArrayList<>();
        for (Tuple tuple : results) {
            Object[] departmentEmployeeData = new Object[]{
                    tuple.get(department.departmentId),
                    tuple.get(department.departmentName),
                    tuple.get(employee.employeeId),
                    tuple.get(employee.employeeName),
                    tuple.get(employee.employeeContactNumber),
                    tuple.get(employee.employeeAddress),
                    tuple.get(employee.employeeGender),
                    tuple.get(employee.employeeSkills),
                    tuple.get(employee.employeeEmail)
            };
            departmentsWithEmployees.add(departmentEmployeeData);
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
