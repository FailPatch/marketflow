package com.avance.marketflow.dao;

import com.avance.marketflow.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EmployeeDao {
    private final AtomicLong ids = new AtomicLong(1);
    private final List<Employee> employees = new ArrayList<>();

    public List<Employee> findAll() {
        return employees;
    }

    public Employee save(String name, String email) {
        Employee employee = new Employee(ids.getAndIncrement(), name, email);
        employees.add(0, employee);
        return employee;
    }
}

