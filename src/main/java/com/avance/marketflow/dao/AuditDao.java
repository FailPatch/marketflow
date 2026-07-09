package com.avance.marketflow.dao;

import com.avance.marketflow.model.AuditLog;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AuditDao {
    private final List<AuditLog> logs = new ArrayList<>();

    public AuditDao() {
        logs.add(new AuditLog("Sistema", "INICIAR", "Aplicacion cargada con datos demo."));
    }

    public void add(String module, String action, String detail) {
        logs.add(0, new AuditLog(module, action, detail));
    }

    public List<AuditLog> findAll() {
        return logs;
    }
}

