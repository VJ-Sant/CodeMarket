package com.codemarket.service;

import com.codemarket.dto.CreateReviewRequest;
import com.codemarket.dto.ReviewResponse;
import com.codemarket.entity.Project;
import com.codemarket.entity.Review;
import com.codemarket.entity.User;
import com.codemarket.repository.ProjectRepository;
import com.codemarket.repository.ReviewRepository;
import com.codemarket.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final PurchaseService purchaseService;

    public ReviewService(ReviewRepository reviewRepository,
                         ProjectRepository projectRepository,
                         UserRepository userRepository,
                         PurchaseService purchaseService) {
        this.reviewRepository = reviewRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.purchaseService = purchaseService;
    }

    public ReviewResponse createReview(String username, Long projectId, CreateReviewRequest request) {
        User reviewer = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (!purchaseService.hasUserPurchasedProject(username, projectId)) {
            throw new IllegalArgumentException("You must purchase the project to review it");
        }

        if (reviewRepository.findByProject_ProjectIdAndReviewer_Username(projectId, username).isPresent()) {
            throw new IllegalArgumentException("You have already reviewed this project");
        }

        Review review = new Review();
        review.setProject(project);
        review.setReviewer(reviewer);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);
        return convertToResponse(savedReview);
    }

    public Page<ReviewResponse> getProjectReviews(Long projectId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByProject_ProjectId(projectId, pageable);
        return reviews.map(this::convertToResponse);
    }

    public ReviewResponse updateReview(String username, Long reviewId, CreateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!review.getReviewer().getUsername().equals(username)) {
            throw new IllegalArgumentException("You can only update your own reviews");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);
        return convertToResponse(updatedReview);
    }

    public void deleteReview(String username, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!review.getReviewer().getUsername().equals(username)) {
            throw new IllegalArgumentException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    private ReviewResponse convertToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setProjectId(review.getProject().getProjectId());
        response.setReviewerUsername(review.getReviewer().getUsername());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setReviewDate(review.getReviewDate());
        return response;
    }
}
