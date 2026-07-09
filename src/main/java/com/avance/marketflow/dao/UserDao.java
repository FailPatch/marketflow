package com.avance.marketflow.dao;

import com.avance.marketflow.model.Role;
import com.avance.marketflow.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserDao {
    private final AtomicLong ids = new AtomicLong(5);
    private final List<User> users = new ArrayList<>();

    public UserDao() {
        User buyer = new User(1, "Comprador Demo", "comprador@demo.com", "123456", Role.BUYER);
        buyer.getCoupons().add("MARKET10");
        users.add(buyer);
        users.add(new User(2, "Vendedor Demo", "vendedor@demo.com", "123456", Role.SELLER));
        users.add(new User(3, "Administrador Demo", "admin@demo.com", "123456", Role.ADMIN));
        users.add(new User(4, "Empleado Demo", "empleado@demo.com", "123456", Role.EMPLOYEE));
    }

    public List<User> findAll() {
        return users;
    }

    public List<User> findSellers() {
        return users.stream().filter(user -> user.getRole() == Role.SELLER).toList();
    }

    public Optional<User> login(String email, String password, Role role) {
        return users.stream()
                .filter(user -> user.isActive())
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .filter(user -> user.getPassword().equals(password))
                .filter(user -> user.getRole() == role)
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public User save(String name, String email, String password, Role role) {
        User user = new User(ids.getAndIncrement(), name, email, password, role);
        if (role == Role.BUYER) {
            user.getCoupons().add("MARKET10");
        }
        users.add(user);
        return user;
    }

    public void delete(String email) {
        users.removeIf(user -> user.getEmail().equalsIgnoreCase(email));
    }
}

