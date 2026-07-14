package com.avance.marketflow.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HelpMessage {
    private final long id;
    private final String name;
    private final String email;
    private final String message;
    private final LocalDateTime createdAt;
    private boolean read;

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
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public String getCreatedAtLabel() {
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
