package com.teleconsys.user_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    // Chiave segreta generata su https://tools.keycdn.com/sha256-online-generatorper firmare e verificare i token JWT
    private static final String secretkey = "b133a0c0e9bee3be20163d2ad31d6248db292aa6dcb1ee087a2aa50e0fc75ae2";

    // Metodo per generare un token JWT
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Aggiunta del campo 'roles' al token JWT
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        claims.put("roles", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        // Generazione del token
        return Jwts.builder()
                .claims() // Informazioni aggiuntive che possono essere incluse nel token
                .add(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 ora
                .and()
                .signWith(getKey()) // Firma il token con la chiave segreta, per garantire che non sia stato modificato
                .compact();
    }

    // Metodo per ottenere la chiave segreta
    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Metodo per estrarre il nome utente dal token JWT
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Metodo generico per estrarre un claim dal token JWT
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Estrae tutti i claims dal token
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Metodo per validare il token (controlla username e scadenza)
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}