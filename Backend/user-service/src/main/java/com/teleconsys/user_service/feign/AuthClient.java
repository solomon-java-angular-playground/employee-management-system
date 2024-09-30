package com.teleconsys.user_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-service", url = "http://localhost:9095")
public interface AuthClient {

    @PostMapping("/api/jwt/generate")
    String generateToken(@RequestParam("username") String username, @RequestParam("password") String password);

    @GetMapping("/api/jwt/validate")
    boolean validateToken(@RequestParam String token, @RequestBody UserDetails userDetails);
}
