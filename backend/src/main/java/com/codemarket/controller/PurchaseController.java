package com.codemarket.controller;

import com.codemarket.dto.PurchaseResponse;
import com.codemarket.service.PurchaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/{projectId}")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<PurchaseResponse> purchaseProject(Authentication authentication,
                                                            @PathVariable Long projectId) {
        PurchaseResponse response = purchaseService.purchaseProject(authentication.getName(), projectId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-purchases")
    @PreAuthorize("hasRole('BUYER') or hasRole('ADMIN')")
    public ResponseEntity<Page<PurchaseResponse>> getMyPurchases(Authentication authentication,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PurchaseResponse> purchases = purchaseService.getBuyerPurchases(authentication.getName(), pageable);
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/my-sales")
    @PreAuthorize("hasRole('SELLER') or hasRole('ADMIN')")
    public ResponseEntity<Page<PurchaseResponse>> getMySales(Authentication authentication,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PurchaseResponse> sales = purchaseService.getSellerSales(authentication.getName(), pageable);
        return ResponseEntity.ok(sales);
    }
}
