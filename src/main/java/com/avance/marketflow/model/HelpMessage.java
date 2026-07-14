package com.avance.marketflow.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HelpMessage {
    private final long id;
    private final String name;
    private final String email;
    private final String message;
    private final LocalDateTime createdAt;
    private String status = "Nuevo";
    private String adminNote = "";
    private String sellerEmail = "";

    public HelpMessage(long id, String name, String email, String message, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.message = message;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAdminNote() { return adminNote; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }
    public String getSellerEmail() { return sellerEmail; }
    public void setSellerEmail(String sellerEmail) { this.sellerEmail = sellerEmail; }
    public boolean isRead() { return !"Nuevo".equalsIgnoreCase(status); }

    public String getCreatedAtLabel() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
