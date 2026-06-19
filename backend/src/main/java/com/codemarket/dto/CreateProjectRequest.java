package com.codemarket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CreateProjectRequest {

    @NotBlank(message = "Project name is required")
    @Size(min = 5, max = 100)
    private String projectName;

    @NotBlank(message = "Project description is required")
    @Size(min = 20, max = 2000)
    private String projectDescription;

    @NotBlank(message = "Technology stack is required")
    private String technologyStack;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double projectPrice;

    private String thumbnailUrl;

    @NotBlank(message = "Difficulty level is required")
    private String difficultyLevel;

    
    private Long categoryId;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getTechnologyStack() {
        return technologyStack;
    }

    public void setTechnologyStack(String technologyStack) {
        this.technologyStack = technologyStack;
    }

    public Double getProjectPrice() {
        return projectPrice;
    }

    public void setProjectPrice(Double projectPrice) {
        this.projectPrice = projectPrice;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
