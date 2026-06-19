package com.codemarket.service;

import com.codemarket.dto.ProjectResponse;
import com.codemarket.entity.Favorite;
import com.codemarket.entity.Project;
import com.codemarket.entity.User;
import com.codemarket.repository.FavoriteRepository;
import com.codemarket.repository.ProjectRepository;
import com.codemarket.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           ProjectRepository projectRepository,
                           UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public void addFavorite(String username, Long projectId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (favoriteRepository.findByUser_UsernameAndProject_ProjectId(username, projectId).isPresent()) {
            throw new IllegalArgumentException("This project is already in your favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProject(project);
        favoriteRepository.save(favorite);
    }

    public void removeFavorite(String username, Long projectId) {
        favoriteRepository.deleteByUser_UsernameAndProject_ProjectId(username, projectId);
    }

    public Page<ProjectResponse> getUserFavorites(String username, Pageable pageable) {
        Page<Favorite> favorites = favoriteRepository.findByUser_Username(username, pageable);
        return favorites.map(favorite -> convertToProjectResponse(favorite.getProject()));
    }

    public boolean isFavorite(String username, Long projectId) {
        return favoriteRepository.findByUser_UsernameAndProject_ProjectId(username, projectId).isPresent();
    }

    private ProjectResponse convertToProjectResponse(Project project) {
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
