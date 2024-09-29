package com.teleconsys.user_service.controller;

import com.teleconsys.user_service.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private UserRoleService userRoleService;

    // Endpoint per assegnare un ruolo a un utente
    @PostMapping("/assign")
    public ResponseEntity<String> assignRoleToUser(@RequestParam Integer userId, @RequestParam String roleName) {
        try {
            userRoleService.assignRoleToUser(userId, roleName);
            return ResponseEntity.ok("Ruolo " + roleName + " assegnato all'utente con ID " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/assign/multiple")
    public ResponseEntity<String> assignRolesToMultipleUsers(@RequestBody Map<Integer, String> userRoleMap) {
        userRoleMap.forEach((userId, roleName) -> {
            try {
                userRoleService.assignRoleToUser(userId, roleName);
            } catch (Exception e) {
                System.out.println("Errore nell'assegnazione dei ruoli: " + e.getMessage());
            }
        });
        return ResponseEntity.ok("Ruoli assegnati agli utenti.");
    }

}


