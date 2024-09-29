package com.teleconsys.department_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teleconsys.department_service.dto.EmployeeDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "department")
@JsonIgnoreProperties({"employees"})
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer departmentId;
    private String departmentName;

    public Department() {
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
}

