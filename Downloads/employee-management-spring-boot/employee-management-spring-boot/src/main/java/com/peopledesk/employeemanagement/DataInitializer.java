package com.peopledesk.employeemanagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final EmployeeRepository repository;

    public DataInitializer(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        repository.saveAll(List.of(
                new Employee("Aarav Sharma", "aarav.sharma@example.com", "Engineering", "Frontend Developer", "Active", LocalDate.parse("2024-02-12")),
                new Employee("Priya Nair", "priya.nair@example.com", "Design", "Product Designer", "Active", LocalDate.parse("2023-11-06")),
                new Employee("Rohan Mehta", "rohan.mehta@example.com", "Engineering", "Backend Developer", "On Leave", LocalDate.parse("2022-08-22")),
                new Employee("Fatima Khan", "fatima.khan@example.com", "Human Resources", "HR Executive", "Active", LocalDate.parse("2024-05-20")),
                new Employee("Daniel Joseph", "daniel.joseph@example.com", "Finance", "Financial Analyst", "Inactive", LocalDate.parse("2021-04-14"))
        ));
    }
}
