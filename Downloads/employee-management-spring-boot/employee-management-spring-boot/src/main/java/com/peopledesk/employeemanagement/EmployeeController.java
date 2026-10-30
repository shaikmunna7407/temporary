package com.peopledesk.employeemanagement;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public Health health() {
        return new Health("ok", "employee-management-demo");
    }

    @GetMapping("/employees")
    public List<Employee> employees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "All") String department,
            @RequestParam(required = false, defaultValue = "All") String status) {
        return service.findAll(search, department, status);
    }

    @GetMapping("/employees/{id}")
    public Employee employee(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/stats")
    public EmployeeService.Stats stats() {
        return service.stats();
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> create(@Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/employees/{id}")
    public Employee update(@PathVariable String id, @Valid @RequestBody EmployeeRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    public record Health(String status, String service) {}
}
