package com.codemarket.repository;

import com.codemarket.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProject_ProjectId(Long projectId, Pageable pageable);
    Optional<Review> findByProject_ProjectIdAndReviewer_Username(Long projectId, String username);
}
