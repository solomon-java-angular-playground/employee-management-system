package com.teleconsys.auth_service.controller;

import com.teleconsys.auth_service.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:4200") // Per accettare richieste provenienti da una specifica origine
@RequestMapping("/api/jwt")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    // Endpoint per autenticare l'utente e generare un token JWT
    @PostMapping("/generate")
    public String generateToken(@RequestParam String username, @RequestParam String password) {
        // Autentica l'utente usando AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        // Se l'autenticazione è avvenuta con successo, genera il token JWT
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtService.generateToken(userDetails);
        } else {
            throw new RuntimeException("Authentication failed");
        }
    }

    // Endpoint per validare un token JWT
    @GetMapping("/validate")
    public boolean validateToken(@RequestParam String token, @RequestParam String username) {
        // Ottiene i dettagli dell'utente e valida il token
        UserDetails userDetails = jwtService.loadUserByUsername(token, username);
        return jwtService.validateToken(token, userDetails);
    }
}