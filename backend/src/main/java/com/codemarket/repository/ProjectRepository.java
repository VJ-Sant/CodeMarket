package com.codemarket.repository;

import com.codemarket.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByCategory_Name(String categoryName, Pageable pageable);
    Page<Project> findByOwner_Username(String username, Pageable pageable);
    Page<Project> findByProjectNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Project> findByDifficultyLevel(String difficultyLevel, Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.projectPrice BETWEEN :minPrice AND :maxPrice")
    Page<Project> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, Pageable pageable);
    
    Page<Project> findAll(Pageable pageable);
}