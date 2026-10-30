package com.peopledesk.employeemanagement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(nullable = false, length = 100)
    private String role;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "joined_date", nullable = false)
    private LocalDate joinedDate;

    protected Employee() {}

    public Employee(String name, String email, String department, String role, String status, LocalDate joinedDate) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
        this.status = status;
        this.joinedDate = joinedDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public LocalDate getJoinedDate() { return joinedDate; }

    public void update(String name, String email, String department, String role, String status, LocalDate joinedDate) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
        this.status = status;
        this.joinedDate = joinedDate;
    }
}
