package com.teleconsys.department_service.dto;

public class DepartmentDTO {
    private Integer departmentId;
    private String departmentName;

    // Costruttore predefinito (senza argomenti)
    public DepartmentDTO() {
    }

    public DepartmentDTO(Integer departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    // Getter e Setter
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
