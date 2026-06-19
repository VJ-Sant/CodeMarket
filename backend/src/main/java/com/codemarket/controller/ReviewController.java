package com.codemarket.controller;

import com.codemarket.dto.CreateReviewRequest;
import com.codemarket.dto.ReviewResponse;
import com.codemarket.service.ReviewService;
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
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/projects/{projectId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<ReviewResponse> createReview(Authentication authentication,
                                                       @PathVariable Long projectId,
                                                       @Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.createReview(authentication.getName(), projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<Page<ReviewResponse>> getProjectReviews(@PathVariable Long projectId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewResponse> reviews = reviewService.getProjectReviews(projectId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<ReviewResponse> updateReview(Authentication authentication,
                                                       @PathVariable Long reviewId,
                                                       @Valid @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.updateReview(authentication.getName(), reviewId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReview(Authentication authentication,
                                             @PathVariable Long reviewId) {
        reviewService.deleteReview(authentication.getName(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
