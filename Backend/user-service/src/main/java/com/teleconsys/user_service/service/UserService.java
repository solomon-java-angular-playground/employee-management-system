package com.teleconsys.user_service.service;

import com.employees.crud.dao.UserDao;
import com.employees.crud.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /*public String register(User user) {
        try {
            // Memorizza la password in chiaro
            String rawPassword = user.getPassword();

            // Cripta la password e salva l'utente nel database
            user.setPassword(encoder.encode(rawPassword));
            userDao.save(user);

            // Autentica l'utente appena registrato
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), rawPassword));

            // Se l'autenticazione è avvenuta con successo, genera un token JWT
            if (auth.isAuthenticated()) {
                return jwtService.generateToken(user.getUsername());
            } else {
                throw new RuntimeException("Authentication failed after registration.");
            }
        } catch (Exception e) {
            // Gestisci l'eccezione e magari logga l'errore
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    public String verify(User user) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (auth.isAuthenticated()) {
                return jwtService.generateToken(user.getUsername());
            } else {
                throw new RuntimeException("Authentication failed.");
            }
        } catch (Exception e) {
            // Gestione dell'eccezione
            throw new RuntimeException("Verification failed: " + e.getMessage(), e);
        }
    }*/

    public String register(User user) {
        try {
            // Memorizza la password in chiaro
            String rawPassword = user.getPassword();

            // Cripta la password e salva l'utente nel database
            user.setPassword(encoder.encode(rawPassword));
            userDao.save(user);

            // Autentica l'utente appena registrato
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), rawPassword));

            // Se l'autenticazione è avvenuta con successo, genera un token JWT
            if (auth.isAuthenticated()) {
                // Ottieni l'oggetto UserDetails dall'Authentication
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                return jwtService.generateToken(userDetails);  // Passa l'oggetto UserDetails
            } else {
                throw new RuntimeException("Authentication failed after registration.");
            }
        } catch (Exception e) {
            // Gestisci l'eccezione e magari logga l'errore
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    public String verify(User user) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (auth.isAuthenticated()) {
                // Ottieni l'oggetto UserDetails dall'Authentication
                UserDetails userDetails = (UserDetails) auth.getPrincipal();
                return jwtService.generateToken(userDetails);  // Passa l'oggetto UserDetails
            } else {
                throw new RuntimeException("Authentication failed.");
            }
        } catch (Exception e) {
            // Gestione dell'eccezione
            throw new RuntimeException("Verification failed: " + e.getMessage(), e);
        }
    }
}

