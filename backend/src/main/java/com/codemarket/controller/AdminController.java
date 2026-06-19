package com.codemarket.controller;

import com.codemarket.dto.ProjectResponse;
import com.codemarket.dto.UserAdminResponse;
import com.codemarket.entity.User;
import com.codemarket.service.AdminService;
import com.codemarket.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ProjectService projectService;

    public AdminController(AdminService adminService, ProjectService projectService) {
        this.adminService = adminService;
        this.projectService = projectService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserAdminResponse>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        List<UserAdminResponse> responses = users.stream()
                .map(user -> new UserAdminResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole().name(),
                        user.isEnabled()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserAdminResponse> getUser(@PathVariable Long userId) {
        User user = adminService.getUserById(userId);
        UserAdminResponse response = new UserAdminResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name(),
                user.isEnabled()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/{userId}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable Long userId) {
        adminService.disableUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/enable")
    public ResponseEntity<Void> enableUser(@PathVariable Long userId) {
        adminService.enableUser(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
