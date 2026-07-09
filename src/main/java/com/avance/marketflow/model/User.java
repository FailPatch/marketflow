package com.avance.marketflow.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private int purchases;
    private int penalties;
    private boolean active = true;
    private final List<String> coupons = new ArrayList<>();
    private final List<String> penaltyReasons = new ArrayList<>();

    public User(long id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public int getPurchases() { return purchases; }
    public int getPenalties() { return penalties; }
    public boolean isActive() { return active; }
    public List<String> getCoupons() { return coupons; }
    public List<String> getPenaltyReasons() { return penaltyReasons; }

    public void setPurchases(int purchases) { this.purchases = purchases; }
    public void setPenalties(int penalties) { this.penalties = penalties; }
    public void setActive(boolean active) { this.active = active; }
}

