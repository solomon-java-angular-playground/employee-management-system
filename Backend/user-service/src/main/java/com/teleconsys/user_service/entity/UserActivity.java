package com.teleconsys.user_service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "user_activity")
public class UserActivity {
    @Id
    private String id;
    private String userId;            // ID dell'utente
    private String action;            // Azione eseguita (es: "login", "aggiunta dipendente")
    private LocalDateTime timestamp;  // Timestamp dell'azione
    private String ipAddress;         // Indirizzo IP dell'utente
    private String details;           // Dettagli aggiuntivi dell'azione (es: ID del dipendente aggiunto)

    // Costruttori, getter e setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
