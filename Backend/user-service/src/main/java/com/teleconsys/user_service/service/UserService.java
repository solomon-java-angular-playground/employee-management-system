package com.teleconsys.user_service.service;

import com.teleconsys.user_service.dao.UserDao;
import com.teleconsys.user_service.entity.User;
import com.teleconsys.user_service.feign.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthClient authClient;  // Per comunicazione con jwtClient

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String register(User user) {
        try {
            // Memorizza la password in chiaro
            String rawPassword = user.getPassword();

            // Cripta la password e salva l'utente nel database
            user.setPassword(encoder.encode(rawPassword));
            userDao.save(user);

            // Invia la richiesta di generazione token all'auth-service via Feign client
            return authClient.generateToken(user.getUsername(), rawPassword);
        } catch (Exception e) {
            // Gestisci l'eccezione e magari logga l'errore
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    public String verify(User user) {
        try {
            // Invia la richiesta di autenticazione all'auth-service via Feign client
            return authClient.generateToken(user.getUsername(), user.getPassword());
        } catch (Exception e) {
            // Gestione dell'eccezione
            throw new RuntimeException("Verification failed: " + e.getMessage(), e);
        }
    }
}

