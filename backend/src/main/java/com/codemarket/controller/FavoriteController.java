package com.codemarket.controller;

import com.codemarket.dto.ProjectResponse;
import com.codemarket.service.FavoriteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{projectId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addFavorite(Authentication authentication,
                                           @PathVariable Long projectId) {
        favoriteService.addFavorite(authentication.getName(), projectId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeFavorite(Authentication authentication,
                                              @PathVariable Long projectId) {
        favoriteService.removeFavorite(authentication.getName(), projectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ProjectResponse>> getUserFavorites(Authentication authentication,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectResponse> favorites = favoriteService.getUserFavorites(authentication.getName(), pageable);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/{projectId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> isFavorite(Authentication authentication,
                                             @PathVariable Long projectId) {
        boolean isFavorite = favoriteService.isFavorite(authentication.getName(), projectId);
        return ResponseEntity.ok(isFavorite);
    }
}
