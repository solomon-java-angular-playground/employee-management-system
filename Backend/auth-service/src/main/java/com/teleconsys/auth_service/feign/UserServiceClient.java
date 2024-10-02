package com.teleconsys.auth_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:9090")
public interface UserServiceClient {

    @GetMapping("/users/{username}")
    UserDetails getUserByUsername(String token, @PathVariable("username") String username);
}

