package com.teleconsys.user_service.service;

import com.teleconsys.user_service.dao.RoleDao;
import com.teleconsys.user_service.dao.UserDao;
import com.teleconsys.user_service.dao.UserRoleDao;
import com.teleconsys.user_service.entity.Role;
import com.teleconsys.user_service.entity.User;
import com.teleconsys.user_service.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private UserDao userDao; // Repository per accedere agli utenti esistenti

    @Autowired
    private RoleDao roleDao; // Repository per accedere ai ruoli esistenti

    // Metodo per assegnare un ruolo a un utente
    public void assignRoleToUser(Integer userId, String roleName) {
        User user = userDao.findById(userId).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        Role role = roleDao.findByName(roleName).orElseThrow(() -> new RuntimeException("Ruolo non trovato"));

        // Verifica se l'utente ha già questo ruolo
        if (userRoleDao.findByUser(user).stream().noneMatch(userRole -> userRole.getRole().equals(role))) {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleDao.save(userRole);
        }
    }

    // Metodo per rimuovere un ruolo da un utente
    public void removeRoleFromUser(Integer userId, String roleName) {
        User user = userDao.findById(userId).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        Role role = roleDao.findByName(roleName).orElseThrow(() -> new RuntimeException("Ruolo non trovato"));

        UserRole userRole = userRoleDao.findByUser(user).stream()
                .filter(ur -> ur.getRole().equals(role))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ruolo non assegnato all'utente"));

        userRoleDao.delete(userRole);
    }

    // Recupera i ruoli di un utente
    public List<Role> getUserRoles(Integer userId) {
        User user = userDao.findById(userId).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return userRoleDao.findByUser(user).stream().map(UserRole::getRole).collect(Collectors.toList());
    }
}