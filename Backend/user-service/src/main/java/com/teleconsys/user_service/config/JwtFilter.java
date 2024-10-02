package com.teleconsys.user_service.config;

import com.teleconsys.user_service.dao.UserDao;
import com.teleconsys.user_service.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

// Filtro applicato a tutte le richieste HTTP, eseguito una volta per ogni richiesta
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtFilter.class.getName());

    @Autowired
    private UserDao userDao;

    @Value("${jwt.secret}")
    private String secretKey;

    // Usato per estrarre lo username dal token JWT e per validare il token
    @Autowired
    private JwtService jwtService;

    // Usato per recuperare i dettagli dell'utente in base allo username
    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Estrazione del token dall'header Authorization
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);

            // Log del token e del nome utente estratto
            // Log dettagliato
            logger.info("Request method: " + request.getMethod());  // Log del metodo della richiesta (es. POST, PUT)
            logger.info("Authorization header: " + authHeader);     // Log del contenuto dell'header Authorization
            logger.info("JWT Token: " + token);
            logger.info("Username from token: " + username);

        }

        // Se lo username è stato estratto dal token e l'utente non è ancora autenticato
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDao.findByUsername(username);

            // Log dei dettagli dell'utente recuperati
            logger.info("User details retrieved: " + userDetails.getUsername());

            // Validazione del token
            if (jwtService.validateToken(token, userDetails)) {
                // Creazione di oggetto con i dettagli dell'utente (comprese le autorizzazioni)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                // Imposta l'utente autenticato nel contesto di sicurezza
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Log dell'autenticazione avvenuta con successo
                logger.info("Authentication successful for user: " + username);
            } else {
                // Log in caso di token non valido
                logger.warning("Invalid token for user: " + username);

            }
        }
        filterChain.doFilter(request, response);
    }
}

