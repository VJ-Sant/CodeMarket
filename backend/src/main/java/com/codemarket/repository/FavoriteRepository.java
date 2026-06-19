package com.codemarket.repository;

import com.codemarket.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Page<Favorite> findByUser_Username(String username, Pageable pageable);
    Optional<Favorite> findByUser_UsernameAndProject_ProjectId(String username, Long projectId);
    void deleteByUser_UsernameAndProject_ProjectId(String username, Long projectId);
}
