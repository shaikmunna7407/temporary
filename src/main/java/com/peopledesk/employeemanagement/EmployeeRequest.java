package com.peopledesk.employeemanagement;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EmployeeRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Email @Size(max = 160) String email,
        @NotBlank @Size(max = 100) String department,
        @NotBlank @Size(max = 100) String role,
        @Pattern(regexp = "Active|On Leave|Inactive", message = "must be Active, On Leave, or Inactive") String status,
        @NotNull LocalDate joinedDate
) {}
