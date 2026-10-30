package com.peopledesk.employeemanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.jayway.jsonpath.JsonPath.read;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:employee-management-test;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc
class EmployeeManagementApplicationTests {
    private static final String VALID_EMPLOYEE = """
            {
              "name": "Samira Ali",
              "email": "samira.ali@example.com",
              "department": "Engineering",
              "role": "Software Engineer",
              "status": "Active",
              "joinedDate": "2025-01-15"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository repository;

    @BeforeEach
    void clearEmployees() {
        repository.deleteAll();
    }

    @Test
    void healthEndpointReturnsOk() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void createUpdateAndDeleteEmployee() throws Exception {
        String response = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_EMPLOYEE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("samira.ali@example.com"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        String employeeId = read(response, "$.id");

        String updatedEmployee = VALID_EMPLOYEE.replace("Samira Ali", "Samira Khan");
        mockMvc.perform(put("/api/employees/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployee))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Samira Khan"));

        mockMvc.perform(delete("/api/employees/{id}", employeeId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsInvalidRequestsAndDuplicateEmails() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_EMPLOYEE.replace("\"Active\"", "\"Unknown\"")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_EMPLOYEE))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_EMPLOYEE.replace("Samira Ali", "Another Employee")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("An employee with this email already exists."));
    }

    @Test
    void filtersEmployeesBySearchAndStatus() throws Exception {
        repository.save(new Employee(
                "Samira Ali", "samira.ali@example.com", "Engineering", "Software Engineer",
                "Active", java.time.LocalDate.parse("2025-01-15")));
        repository.save(new Employee(
                "Ravi Kumar", "ravi.kumar@example.com", "Finance", "Analyst",
                "Inactive", java.time.LocalDate.parse("2024-06-01")));

        mockMvc.perform(get("/api/employees")
                        .param("search", "samira")
                        .param("status", "Active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Samira Ali"));
    }
}
