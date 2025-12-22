package com.example.taskask;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskask.dto.CreateTaskRequest;
import com.example.taskask.dto.TaskResponse;
import com.example.taskask.dto.UpdateTaskStatusRequest;
import com.example.taskask.entity.EmployeeProfile;
import com.example.taskask.entity.Task;
import com.example.taskask.entity.TaskAssignment;
import com.example.taskask.entity.User;
import com.example.taskask.enums.Role;
import com.example.taskask.enums.TaskAssignmentStatus;
import com.example.taskask.enums.TaskPriority;
import com.example.taskask.enums.TaskStatus;
import com.example.taskask.repository.EmployeeProfileRepository;
import com.example.taskask.repository.TaskAssignmentRepository;
import com.example.taskask.repository.TaskRepository;
import com.example.taskask.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "app.jwt.secret=test-secret-test-secret-test-secret-123456",
        "app.jwt.expiration-ms=3600000"
})
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAssignmentRepository taskAssignmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        taskAssignmentRepository.deleteAll();
        taskRepository.deleteAll();
        employeeProfileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
        void authRegisterReturnsToken() throws Exception {
                String payload = "{\"email\":\"newuser@example.com\",\"password\":\"secret123\",\"fullName\":\"New User\"}";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value("newuser@example.com"));
    }

    @Test
    @WithMockUser(username = "bob@example.com", roles = "EMPLOYEE")
    void employeeCannotUpdateUnassignedTask() throws Exception {
        User admin = saveUser("admin@example.com", Role.ADMIN);
        EmployeeProfile alice = saveEmployee("alice@example.com", Role.EMPLOYEE, null);
        saveEmployee("bob@example.com", Role.EMPLOYEE, null);

        CreateTaskRequest request = CreateTaskRequest.builder()
                .title("Task 1")
                .priority(TaskPriority.MEDIUM)
                .assigneeIds(List.of(alice.getId()))
                .build();

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .principal(() -> admin.getEmail())
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user(admin.getEmail()).roles("ADMIN")))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TaskResponse taskResponse = objectMapper.readValue(response, TaskResponse.class);

        UpdateTaskStatusRequest update = new UpdateTaskStatusRequest(TaskStatus.COMPLETED);
        mockMvc.perform(patch("/api/tasks/" + taskResponse.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update))
                        .principal(() -> "bob@example.com")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("bob@example.com").roles("EMPLOYEE")))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "alice@example.com", roles = "EMPLOYEE")
    void dashboardShowsCountsForEmployee() throws Exception {
        EmployeeProfile alice = saveEmployee("alice@example.com", Role.EMPLOYEE, null);
        Task openTask = taskRepository.save(Task.builder()
                .title("Open Task")
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.OPEN)
                .dueDate(LocalDate.now().plusDays(2))
                .build());
        Task doneTask = taskRepository.save(Task.builder()
                .title("Done Task")
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.COMPLETED)
                .dueDate(LocalDate.now())
                .build());
        taskAssignmentRepository.save(TaskAssignment.builder()
                .task(openTask)
                .assignee(alice)
                .assignmentStatus(TaskAssignmentStatus.ASSIGNED)
                .build());
        taskAssignmentRepository.save(TaskAssignment.builder()
                .task(doneTask)
                .assignee(alice)
                .assignmentStatus(TaskAssignmentStatus.ASSIGNED)
                .build());

        mockMvc.perform(get("/api/tasks/dashboard")
                        .principal(() -> "alice@example.com")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("alice@example.com").roles("EMPLOYEE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.open").value(1))
                .andExpect(jsonPath("$.completed").value(1));
    }

    @Test
    @WithMockUser(username = "alice@example.com", roles = "EMPLOYEE")
    void reportReturnsCompletionAndOverdue() throws Exception {
        EmployeeProfile alice = saveEmployee("alice@example.com", Role.EMPLOYEE, null);

        Task overdue = taskRepository.save(Task.builder()
                .title("Overdue")
                .priority(TaskPriority.LOW)
                .status(TaskStatus.IN_PROGRESS)
                .dueDate(LocalDate.now().minusDays(3))
                .build());
        Task completed = taskRepository.save(Task.builder()
                .title("Done")
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.COMPLETED)
                .dueDate(LocalDate.now().minusMonths(1))
                .build());
        taskAssignmentRepository.save(TaskAssignment.builder()
                .task(overdue)
                .assignee(alice)
                .assignmentStatus(TaskAssignmentStatus.ASSIGNED)
                .build());
        taskAssignmentRepository.save(TaskAssignment.builder()
                .task(completed)
                .assignee(alice)
                .assignmentStatus(TaskAssignmentStatus.ASSIGNED)
                .build());

        String response = mockMvc.perform(get("/api/reports/employee/" + alice.getId())
                        .principal(() -> "alice@example.com")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("alice@example.com").roles("EMPLOYEE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTasks").value(2))
                .andExpect(jsonPath("$.completedTasks").value(1))
                .andExpect(jsonPath("$.overdueTasks").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();

        com.example.taskask.dto.EmployeeReportResponse report = objectMapper.readValue(response, com.example.taskask.dto.EmployeeReportResponse.class);
        assertThat(report.getCompletionRate()).isBetween(0.49, 0.51);
        assertThat(report.getMonthlySummary()).isNotEmpty();
    }

    private User saveUser(String email, Role role) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode("password"))
                .role(role)
                .active(true)
                                .fullName(email)
                .build();
        return userRepository.save(user);
    }

        private EmployeeProfile saveEmployee(String email, Role role, EmployeeProfile manager) {
                User user = User.builder()
                                .email(email)
                                .password(passwordEncoder.encode("password"))
                                .role(role)
                                .active(true)
                                .fullName(email)
                                .build();
                EmployeeProfile profile = EmployeeProfile.builder()
                                .user(user)
                                .fullName(email)
                                .manager(manager)
                                .build();
                return employeeProfileRepository.save(profile);
        }
}
