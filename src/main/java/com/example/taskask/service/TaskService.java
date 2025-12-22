package com.example.taskask.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskask.dto.CreateTaskRequest;
import com.example.taskask.dto.EmployeeReportResponse;
import com.example.taskask.dto.MonthlySummaryResponse;
import com.example.taskask.dto.TaskCommentRequest;
import com.example.taskask.dto.TaskCommentResponse;
import com.example.taskask.dto.TaskDashboardResponse;
import com.example.taskask.dto.TaskResponse;
import com.example.taskask.entity.EmployeeProfile;
import com.example.taskask.entity.Task;
import com.example.taskask.entity.TaskAssignment;
import com.example.taskask.entity.TaskComment;
import com.example.taskask.entity.User;
import com.example.taskask.enums.Role;
import com.example.taskask.enums.TaskAssignmentStatus;
import com.example.taskask.enums.TaskStatus;
import com.example.taskask.repository.EmployeeProfileRepository;
import com.example.taskask.repository.TaskAssignmentRepository;
import com.example.taskask.repository.TaskCommentRepository;
import com.example.taskask.repository.TaskRepository;
import com.example.taskask.repository.TaskRepository.MonthlySummaryProjection;
import com.example.taskask.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    private final TaskRepository taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request) {
        if (!request.isDateRangeValid()) {
            throw new IllegalArgumentException("Due date cannot be before start date");
        }

        // Restrict manager to assign tasks only to their own team
        User currentUser = null;
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User userDetails) {
            String email = userDetails.getUsername();
            currentUser = userRepository.findByEmail(email).orElse(null);
        }

        Task task = Task.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .priority(request.getPriority())
            .status(TaskStatus.OPEN)
            .startDate(request.getStartDate())
            .dueDate(request.getDueDate())
            .build();

        Task saved = taskRepository.save(task);

        List<EmployeeProfile> assignees = employeeProfileRepository.findAllById(request.getAssigneeIds());
        if (assignees.size() != request.getAssigneeIds().size()) {
            throw new IllegalArgumentException("One or more assignees not found");
        }

        // If current user is MANAGER, check all assignees are in their team
        if (currentUser != null && currentUser.getRole() == Role.MANAGER) {
            EmployeeProfile managerProfile = employeeProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new IllegalStateException("Manager profile not found"));
            boolean allInTeam = assignees.stream().allMatch(a ->
                a.getManager() != null && a.getManager().getId().equals(managerProfile.getId())
                || a.getId().equals(managerProfile.getId())
            );
            if (!allInTeam) {
            throw new AccessDeniedException("Managers can only assign tasks to their own team members");
            }
        }

        List<TaskAssignment> assignments = assignees.stream()
            .map(a -> TaskAssignment.builder()
                .task(saved)
                .assignee(a)
                .assignmentStatus(TaskAssignmentStatus.ASSIGNED)
                .build())
            .toList();
        taskAssignmentRepository.saveAll(assignments);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> getTasks(TaskStatus status, Long assigneeId, int page, int size, String requesterEmail) {
        User requester = requireUser(requesterEmail);
        Pageable pageable = resolvePageable(page, size);

        if (requester.getRole() == Role.ADMIN) {
            return fetchAdminTasks(status, assigneeId, pageable).map(this::toResponse);
        }

        EmployeeProfile profile = requireEmployeeProfile(requester.getId());
        if (requester.getRole() == Role.MANAGER) {
            if (assigneeId != null && !assigneeManagedBy(profile, assigneeId)) {
                throw new AccessDeniedException("Managers can only view tasks for their team");
            }
            return taskRepository.findByManagerIdAndStatus(profile.getId(), status, pageable)
                    .map(this::toResponse);
        }

        if (assigneeId != null && !assigneeId.equals(profile.getId())) {
            throw new AccessDeniedException("Employees can only view tasks assigned to them");
        }
        return taskRepository.findByAssigneeIdAndStatus(profile.getId(), status, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public TaskResponse updateStatus(Long id, TaskStatus status, String requesterEmail) {
        User requester = requireUser(requesterEmail);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        assertCanAccessTask(task, requester);
        task.setStatus(status);
        Task updated = taskRepository.save(task);
        return toResponse(updated);
    }

    @Transactional
    public TaskCommentResponse addComment(Long taskId, TaskCommentRequest request, String authorEmail) {
        User author = requireUser(authorEmail);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        assertCanAccessTask(task, author);

        TaskComment comment = TaskComment.builder()
                .task(task)
                .author(author)
                .comment(request.getComment())
                .build();
        TaskComment saved = taskCommentRepository.save(comment);
        return TaskCommentResponse.builder()
                .id(saved.getId())
                .authorEmail(author.getEmail())
                .comment(saved.getComment())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<TaskCommentResponse> getComments(Long taskId, String requesterEmail) {
        User requester = requireUser(requesterEmail);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        assertCanAccessTask(task, requester);

        return taskCommentRepository.findByTaskId(taskId).stream()
                .map(c -> TaskCommentResponse.builder()
                        .id(c.getId())
                        .authorEmail(c.getAuthor().getEmail())
                        .comment(c.getComment())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskDashboardResponse getDashboard(String requesterEmail) {
        User requester = requireUser(requesterEmail);
        if (requester.getRole() == Role.ADMIN) {
            return buildDashboardCounts(
                    taskRepository.countByStatus(TaskStatus.OPEN),
                    taskRepository.countByStatus(TaskStatus.IN_PROGRESS),
                    taskRepository.countByStatus(TaskStatus.COMPLETED),
                    taskRepository.countByStatus(TaskStatus.BLOCKED));
        }

        EmployeeProfile profile = requireEmployeeProfile(requester.getId());
        if (requester.getRole() == Role.MANAGER) {
            return buildDashboardCounts(
                    taskRepository.countByManagerIdAndStatus(profile.getId(), TaskStatus.OPEN),
                    taskRepository.countByManagerIdAndStatus(profile.getId(), TaskStatus.IN_PROGRESS),
                    taskRepository.countByManagerIdAndStatus(profile.getId(), TaskStatus.COMPLETED),
                    taskRepository.countByManagerIdAndStatus(profile.getId(), TaskStatus.BLOCKED));
        }

        return buildDashboardCounts(
                taskRepository.countByAssigneeIdAndStatus(profile.getId(), TaskStatus.OPEN),
                taskRepository.countByAssigneeIdAndStatus(profile.getId(), TaskStatus.IN_PROGRESS),
                taskRepository.countByAssigneeIdAndStatus(profile.getId(), TaskStatus.COMPLETED),
                taskRepository.countByAssigneeIdAndStatus(profile.getId(), TaskStatus.BLOCKED));
    }

        @Transactional(readOnly = true)
        public EmployeeReportResponse getEmployeeReport(Long employeeId, String requesterEmail) {
        User requester = requireUser(requesterEmail);
        EmployeeProfile target = employeeProfileRepository.findById(employeeId)
            .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        if (!canViewEmployee(target, requester)) {
            throw new AccessDeniedException("You do not have access to this employee report");
        }

        long total = taskRepository.countByAssigneeId(employeeId);
        long completed = taskRepository.countByAssigneeIdAndStatus(employeeId, TaskStatus.COMPLETED);
        long overdue = taskRepository.countOverdueByAssignee(employeeId, LocalDate.now());
        double completionRate = total == 0 ? 0.0 : (double) completed / total;

        List<MonthlySummaryResponse> monthly = taskRepository.monthlySummaryByAssignee(employeeId).stream()
            .map(this::toMonthlySummary)
            .toList();

        return EmployeeReportResponse.builder()
            .totalTasks(total)
            .completedTasks(completed)
            .completionRate(completionRate)
            .overdueTasks(overdue)
            .monthlySummary(monthly)
            .build();
        }

    private TaskDashboardResponse buildDashboardCounts(long open, long inProgress, long completed, long blocked) {
        return TaskDashboardResponse.builder()
                .open(open)
                .inProgress(inProgress)
                .completed(completed)
                .blocked(blocked)
                .build();
    }

    private MonthlySummaryResponse toMonthlySummary(MonthlySummaryProjection projection) {
        long total = projection.getTotal() != null ? projection.getTotal() : 0L;
        long completed = projection.getCompleted() != null ? projection.getCompleted() : 0L;
        double rate = total == 0 ? 0.0 : (double) completed / total;
        return MonthlySummaryResponse.builder()
                .year(projection.getYear())
                .month(projection.getMonth())
                .total(total)
                .completed(completed)
                .completionRate(rate)
                .build();
    }

    private Page<Task> fetchAdminTasks(TaskStatus status, Long assigneeId, Pageable pageable) {
        if (assigneeId != null) {
            return taskRepository.findByAssigneeIdAndStatus(assigneeId, status, pageable);
        }
        if (status != null) {
            return taskRepository.findByStatus(status, pageable);
        }
        return taskRepository.findAll(pageable);
    }

    private void assertCanAccessTask(Task task, User user) {
        if (user.getRole() == Role.ADMIN) {
            return;
        }
        if (user.getRole() == Role.EMPLOYEE) {
            boolean allowed = taskAssignmentRepository.existsByTaskIdAndAssigneeUserEmail(task.getId(), user.getEmail());
            if (!allowed) {
                throw new AccessDeniedException("You do not have access to this task");
            }
            return;
        }
        EmployeeProfile managerProfile = requireEmployeeProfile(user.getId());
        boolean allowed = taskAssignmentRepository.findByTaskId(task.getId()).stream()
                .map(TaskAssignment::getAssignee)
                .anyMatch(assignee -> assignee.getId().equals(managerProfile.getId())
                        || (assignee.getManager() != null && assignee.getManager().getId().equals(managerProfile.getId())));
        if (!allowed) {
            throw new AccessDeniedException("You do not have access to this task");
        }
    }

    private boolean canViewEmployee(EmployeeProfile target, User requester) {
        if (requester.getRole() == Role.ADMIN) {
            return true;
        }
        if (requester.getRole() == Role.EMPLOYEE) {
            return target.getUser().getId().equals(requester.getId());
        }
        EmployeeProfile managerProfile = requireEmployeeProfile(requester.getId());
        return target.getId().equals(managerProfile.getId())
                || (target.getManager() != null && target.getManager().getId().equals(managerProfile.getId()));
    }

    private boolean assigneeManagedBy(EmployeeProfile managerProfile, Long assigneeId) {
        return employeeProfileRepository.findById(assigneeId)
                .map(emp -> emp.getId().equals(managerProfile.getId())
                        || (emp.getManager() != null && emp.getManager().getId().equals(managerProfile.getId())))
                .orElse(false);
    }

    private Pageable resolvePageable(int page, int size) {
        int pageNumber = Math.max(0, page);
        int pageSize = size > 0 ? Math.min(size, MAX_PAGE_SIZE) : DEFAULT_PAGE_SIZE;
        return PageRequest.of(pageNumber, pageSize);
    }

    private User requireUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private EmployeeProfile requireEmployeeProfile(Long userId) {
        return employeeProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Employee profile not found for user"));
    }

    private TaskResponse toResponse(Task task) {
        List<String> assigneeNames = taskAssignmentRepository.findByTaskId(task.getId()).stream()
                .map(ta -> ta.getAssignee().getFullName())
                .toList();
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .startDate(task.getStartDate())
                .dueDate(task.getDueDate())
                .assigneeNames(assigneeNames)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
