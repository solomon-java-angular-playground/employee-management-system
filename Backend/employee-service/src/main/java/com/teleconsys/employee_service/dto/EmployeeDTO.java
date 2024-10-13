package com.teleconsys.employee_service.dto;

public class EmployeeDTO {
    private Integer employeeId;
    private String employeeName;
    private String employeeSkills;
    private String employeeEmail;
    private Integer employeeDepartmentId;

    // Costruttore, getter e setter
    public EmployeeDTO() {
    }

    public EmployeeDTO(Integer employeeId, String employeeName, String employeeSkills, String employeeEmail, Integer employeeDepartmentId) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeSkills = employeeSkills;
        this.employeeEmail = employeeEmail;
        this.employeeDepartmentId = employeeDepartmentId;
    }

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

    public String getEmployeeSkills() {
        return employeeSkills;
    }

    public void setEmployeeSkills(String employeeSkills) {
        this.employeeSkills = employeeSkills;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public Integer getEmployeeDepartmentId() {
        return employeeDepartmentId;
    }

    public void setEmployeeDepartmentId(Integer employeeDepartmentId) {
        this.employeeDepartmentId = employeeDepartmentId;
    }
}
