package com.avance.marketflow.service;

import com.avance.marketflow.dao.AuditDao;
import com.avance.marketflow.dao.UserDao;
import com.avance.marketflow.model.Role;
import com.avance.marketflow.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserDao userDao;
    private final AuditDao auditDao;

    public AuthService(UserDao userDao, AuditDao auditDao) {
        this.userDao = userDao;
        this.auditDao = auditDao;
    }

    public Optional<User> login(String email, String password, Role role) {
        Optional<User> user = userDao.login(email, password, role);
        user.ifPresent(value -> auditDao.add("Seguridad", "LOGIN", value.getEmail() + " ingreso como " + value.getRole()));
        return user;
    }

    public User register(String name, String email, String password, Role role) {
        User user = userDao.save(name, email, password, role);
        auditDao.add("Seguridad", "CREAR_USUARIO", email + " registrado como " + role);
        return user;
    }
}

