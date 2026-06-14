package com.codemarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codemarket.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}