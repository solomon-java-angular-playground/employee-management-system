package com.teleconsys.employee_service.feign;

import com.teleconsys.employee_service.dto.DepartmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "department-service", url = "http://localhost:9080")
public interface DepartmentClient {

    @GetMapping("/departments/{departmentId}")
    DepartmentDTO getDepartmentById(@PathVariable("departmentId") Integer departmentId);
}

