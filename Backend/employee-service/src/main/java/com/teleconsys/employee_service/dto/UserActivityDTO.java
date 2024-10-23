package com.teleconsys.employee_service.dto;

public class UserActivityDTO {
    Integer userId;
    private String action;
    private String ipAddress;

    public UserActivityDTO(Integer userId, String action, String ipAddress) {
        this.action = action;
        this.ipAddress = ipAddress;
    }

    // Costruttori, getter e setter
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
