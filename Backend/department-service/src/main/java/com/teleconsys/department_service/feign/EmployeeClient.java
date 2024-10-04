package com.teleconsys.department_service.feign;

import com.teleconsys.department_service.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employee-service", url = "http://localhost:9085")
public interface EmployeeClient {
    @GetMapping("/employees/by-department/{departmentId}")
    List<EmployeeDTO> getEmployeesByDepartmentId(@PathVariable("departmentId") Integer departmentId);
}

