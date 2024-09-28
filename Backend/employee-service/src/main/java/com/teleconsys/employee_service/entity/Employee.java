package com.teleconsys.employee_service.entity;

// Importa le annotazioni e le classi necessarie di JPA per il mapping
// dell'entità al database, come Entity, Table, Id, GeneratedValue

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "employee")
@JsonIgnoreProperties({"employees"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer employeeId;
    private String employeeName;
    private String employeeContactNumber;
    private String employeeAddress;
    private String employeeGender;
    private String employeeSkills;
    private String employeeEmail; // Nuovo campo per l'email
    private Integer employeeDepartmentId;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // Costruttore di default, getter e setter
    public Employee() { }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeContactNumber() {
        return employeeContactNumber;
    }

    public void setEmployeeContactNumber(String employeeContactNumber) {
        this.employeeContactNumber = employeeContactNumber;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeGender() {
        return employeeGender;
    }

    public void setEmployeeGender(String employeeGender) {
        this.employeeGender = employeeGender;
    }

    public String getEmployeeSkills() {
        return employeeSkills;
    }

    public void setEmployeeSkills(String employeeSkills) {
        this.employeeSkills = employeeSkills;
    }

    public Integer getEmployeeDepartmentId() {
        return employeeDepartmentId;
    }

    public void setEmployeeDepartmentId(Integer employeeDepartmentId) {
        this.employeeDepartmentId = employeeDepartmentId;
    }

    public Department getEmployeeDepartment() {
        return department;
    }

    public void setEmployeeDepartment(Department department) {
        this.department = department;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }
}


