package com.teleconsys.department_service.dto;

public class EmployeeDTO {
    private Integer employeeId;
    private String employeeName;
    private String employeeContactNumber;
    private String employeeAddress;
    private String employeeGender;
    private String employeeSkills;
    private String employeeEmail;
    private Integer employeeDepartmentId;

 /*   public EmployeeDTO() {
    }*/

/*    public EmployeeDTO(Integer employeeId, String employeeName, String employeeContactNumber,
                       String employeeAddress, String employeeGender, String employeeSkills,
                       String employeeEmail, Integer employeeDepartmentId) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeContactNumber = employeeContactNumber;
        this.employeeAddress = employeeAddress;
        this.employeeGender = employeeGender;
        this.employeeSkills = employeeSkills;
        this.employeeEmail = employeeEmail;
        this.employeeDepartmentId = employeeDepartmentId;
    }*/

    // Getters e Setters
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

