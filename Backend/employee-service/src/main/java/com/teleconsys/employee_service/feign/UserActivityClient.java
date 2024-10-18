package com.teleconsys.employee_service.feign;

import com.teleconsys.employee_service.dto.UserActivityDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:9090")
public interface UserActivityClient {

    @PostMapping("/tracking")
    void trackActivity(@RequestBody UserActivityDTO userActivity);
}
