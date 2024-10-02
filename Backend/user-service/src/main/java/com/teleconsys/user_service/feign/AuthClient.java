package com.teleconsys.user_service.feign;

import com.teleconsys.user_service.dto.AuthRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-service", url = "http://localhost:9095")
public interface AuthClient {

    @PostMapping("/api/jwt/generate")
    String generateToken(@RequestBody AuthRequest authRequest);

    @GetMapping("/api/jwt/validate")
    boolean validateToken(@RequestParam String token, @RequestParam String username);
}
