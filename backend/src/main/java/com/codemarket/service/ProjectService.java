package com.codemarket.service;

import com.codemarket.dto.CreateProjectRequest;
import com.codemarket.dto.ProjectResponse;
import com.codemarket.dto.UpdateProjectRequest;
import com.codemarket.entity.Category;
import com.codemarket.entity.Project;
import com.codemarket.entity.User;
import com.codemarket.repository.CategoryRepository;
import com.codemarket.repository.ProjectRepository;
import com.codemarket.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponse createProject(String username, CreateProjectRequest request) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Project project = new Project();
        project.setProjectName(request.getProjectName());
        project.setProjectDescription(request.getProjectDescription());
        project.setTechnologyStack(request.getTechnologyStack());
        project.setProjectPrice(request.getProjectPrice());
        project.setThumbnailUrl(request.getThumbnailUrl());
        project.setDifficultyLevel(request.getDifficultyLevel());
        project.setCategory(category);
        project.setOwner(owner);

        Project savedProject = projectRepository.save(project);
        return convertToResponse(savedProject);
    }

    public ProjectResponse updateProject(String username, Long projectId, UpdateProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (!project.getOwner().getUsername().equals(username)) {
            throw new IllegalArgumentException("You can only update your own projects");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        project.setProjectName(request.getProjectName());
        project.setProjectDescription(request.getProjectDescription());
        project.setTechnologyStack(request.getTechnologyStack());
        project.setProjectPrice(request.getProjectPrice());
        project.setThumbnailUrl(request.getThumbnailUrl());
        project.setDifficultyLevel(request.getDifficultyLevel());
        project.setCategory(category);

        Project updatedProject = projectRepository.save(project);
        return convertToResponse(updatedProject);
    }

    public void deleteProject(String username, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (!project.getOwner().getUsername().equals(username)) {
            throw new IllegalArgumentException("You can only delete your own projects");
        }

        projectRepository.delete(project);
    }

    public ProjectResponse getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        return convertToResponse(project);
    }

    public Page<ProjectResponse> getAllProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.map(this::convertToResponse);
    }

    public Page<ProjectResponse> searchByName(String name, Pageable pageable) {
        Page<Project> projects = projectRepository.findByProjectNameContainingIgnoreCase(name, pageable);
        return projects.map(this::convertToResponse);
    }

    public Page<ProjectResponse> searchByCategory(String categoryName, Pageable pageable) {
        Page<Project> projects = projectRepository.findByCategory_Name(categoryName, pageable);
        return projects.map(this::convertToResponse);
    }

    public Page<ProjectResponse> searchByDifficulty(String difficulty, Pageable pageable) {
        Page<Project> projects = projectRepository.findByDifficultyLevel(difficulty, pageable);
        return projects.map(this::convertToResponse);
    }

    public Page<ProjectResponse> searchByPriceRange(Double minPrice, Double maxPrice, Pageable pageable) {
        Page<Project> projects = projectRepository.findByPriceRange(minPrice, maxPrice, pageable);
        return projects.map(this::convertToResponse);
    }

    public Page<ProjectResponse> getProjectsByOwner(String username, Pageable pageable) {
        Page<Project> projects = projectRepository.findByOwner_Username(username, pageable);
        return projects.map(this::convertToResponse);
    }

    private ProjectResponse convertToResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(project.getProjectId());
        response.setProjectName(project.getProjectName());
        response.setProjectDescription(project.getProjectDescription());
        response.setTechnologyStack(project.getTechnologyStack());
        response.setProjectPrice(project.getProjectPrice());
        response.setThumbnailUrl(project.getThumbnailUrl());
        response.setDifficultyLevel(project.getDifficultyLevel());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        response.setCategoryName(project.getCategory().getName());
        response.setCategoryId(project.getCategory().getId());
        response.setOwnerUsername(project.getOwner().getUsername());
        response.setOwnerId(project.getOwner().getId());
        return response;
    }
}
