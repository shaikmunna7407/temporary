package com.peopledesk.employeemanagement;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll(String search, String department, String status) {
        List<Employee> employees;
        String query = search == null ? "" : search.trim();
        if (query.isBlank()) {
            employees = repository.findAll();
        } else {
            employees = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrRoleContainingIgnoreCaseOrDepartmentContainingIgnoreCase(
                    query, query, query, query);
        }

        return employees.stream()
                .filter(e -> department == null || department.isBlank() || "All".equalsIgnoreCase(department)
                        || e.getDepartment().equalsIgnoreCase(department))
                .filter(e -> status == null || status.isBlank() || "All".equalsIgnoreCase(status)
                        || e.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    @Transactional(readOnly = true)
    public Employee findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found."));
    }

    public Employee create(EmployeeRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An employee with this email already exists.");
        }
        return repository.save(new Employee(
                request.name().trim(), email, request.department().trim(), request.role().trim(),
                normalizeStatus(request.status()), request.joinedDate()));
    }

    public Employee update(String id, EmployeeRequest request) {
        Employee employee = findById(id);
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        repository.findByEmailIgnoreCase(email).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "An employee with this email already exists.");
            }
        });
        employee.update(request.name().trim(), email, request.department().trim(), request.role().trim(),
                normalizeStatus(request.status()), request.joinedDate());
        return repository.save(employee);
    }

    public void delete(String id) {
        Employee employee = findById(id);
        repository.delete(employee);
    }

    @Transactional(readOnly = true)
    public Stats stats() {
        List<Employee> all = repository.findAll();
        long active = all.stream().filter(e -> "Active".equals(e.getStatus())).count();
        long onLeave = all.stream().filter(e -> "On Leave".equals(e.getStatus())).count();
        long departments = all.stream().map(Employee::getDepartment).distinct().count();
        return new Stats(all.size(), active, onLeave, departments);
    }

    private String normalizeStatus(String status) {
        return List.of("Active", "On Leave", "Inactive").contains(status) ? status : "Active";
    }

    public record Stats(long total, long active, long onLeave, long departments) {}
}
