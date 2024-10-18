package com.teleconsys.employee_service.feign;

import com.teleconsys.employee_service.dto.DepartmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "department-service", url = "http://localhost:9080")
public interface DepartmentClient {

    @GetMapping("/departments/{departmentId}")
    DepartmentDTO getDepartmentById(@PathVariable("departmentId") Integer departmentId);

    @PostMapping("/departments")
    DepartmentDTO saveDepartment(@RequestBody DepartmentDTO departmentDTO);
}

