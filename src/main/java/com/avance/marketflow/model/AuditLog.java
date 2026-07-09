package com.avance.marketflow.model;

public class AuditLog {
    private String module;
    private String action;
    private String detail;

    public AuditLog(String module, String action, String detail) {
        this.module = module;
        this.action = action;
        this.detail = detail;
    }

    public String getModule() { return module; }
    public String getAction() { return action; }
    public String getDetail() { return detail; }
}

