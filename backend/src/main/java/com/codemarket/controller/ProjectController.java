package com.codemarket.controller;

import com.codemarket.dto.CreateProjectRequest;
import com.codemarket.dto.ProjectResponse;
import com.codemarket.dto.UpdateProjectRequest;
import com.codemarket.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> createProject(Authentication authentication,
                                                         @Valid @RequestBody CreateProjectRequest request) {
        ProjectResponse response = projectService.createProject(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{projectId}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<ProjectResponse> updateProject(Authentication authentication,
                                                         @PathVariable Long projectId,
                                                         @Valid @RequestBody UpdateProjectRequest request) {
        ProjectResponse response = projectService.updateProject(authentication.getName(), projectId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(Authentication authentication,
                                              @PathVariable Long projectId) {
        projectService.deleteProject(authentication.getName(), projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long projectId) {
        ProjectResponse response = projectService.getProjectById(projectId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> projects = projectService.getAllProjects(pageable);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/search/name")
    public ResponseEntity<Page<ProjectResponse>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> projects = projectService.searchByName(name, pageable);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/search/category")
    public ResponseEntity<Page<ProjectResponse>> searchByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> projects = projectService.searchByCategory(category, pageable);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/search/difficulty")
    public ResponseEntity<Page<ProjectResponse>> searchByDifficulty(
            @RequestParam String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> projects = projectService.searchByDifficulty(difficulty, pageable);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/search/price")
    public ResponseEntity<Page<ProjectResponse>> searchByPrice(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> projects = projectService.searchByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/owner/{username}")
    public ResponseEntity<Page<ProjectResponse>> getProjectsByOwner(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> projects = projectService.getProjectsByOwner(username, pageable);
        return ResponseEntity.ok(projects);
    }
}
