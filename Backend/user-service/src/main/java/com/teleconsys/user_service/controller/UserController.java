package com.teleconsys.user_service.controller;

import com.teleconsys.user_service.entity.User;
import com.teleconsys.user_service.service.MyUserDetailsService;
import com.teleconsys.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:4200") // Per accettare richieste provenienti da una specifica origine

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.verify(user);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<UserDetails> getUserByUsername(@PathVariable String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return ResponseEntity.ok(userDetails);
    }
}

