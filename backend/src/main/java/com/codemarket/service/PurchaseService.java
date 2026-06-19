package com.codemarket.service;

import com.codemarket.dto.PurchaseResponse;
import com.codemarket.entity.Project;
import com.codemarket.entity.Purchase;
import com.codemarket.entity.User;
import com.codemarket.repository.ProjectRepository;
import com.codemarket.repository.PurchaseRepository;
import com.codemarket.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public PurchaseService(PurchaseRepository purchaseRepository,
                           ProjectRepository projectRepository,
                           UserRepository userRepository) {
        this.purchaseRepository = purchaseRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public PurchaseResponse purchaseProject(String username, Long projectId) {
        User buyer = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (purchaseRepository.findByBuyer_UsernameAndProject_ProjectId(username, projectId).isPresent()) {
            throw new IllegalArgumentException("You have already purchased this project");
        }

        if (project.getOwner().getId().equals(buyer.getId())) {
            throw new IllegalArgumentException("You cannot purchase your own project");
        }

        Purchase purchase = new Purchase();
        purchase.setBuyer(buyer);
        purchase.setProject(project);
        purchase.setAmount(project.getProjectPrice());

        Purchase savedPurchase = purchaseRepository.save(purchase);
        return convertToResponse(savedPurchase);
    }

    public Page<PurchaseResponse> getBuyerPurchases(String username, Pageable pageable) {
        Page<Purchase> purchases = purchaseRepository.findByBuyer_Username(username, pageable);
        return purchases.map(this::convertToResponse);
    }

    public Page<PurchaseResponse> getSellerSales(String username, Pageable pageable) {
        Page<Purchase> purchases = purchaseRepository.findByProject_Owner_Username(username, pageable);
        return purchases.map(this::convertToResponse);
    }

    public boolean hasUserPurchasedProject(String username, Long projectId) {
        return purchaseRepository.findByBuyer_UsernameAndProject_ProjectId(username, projectId).isPresent();
    }

    private PurchaseResponse convertToResponse(Purchase purchase) {
        PurchaseResponse response = new PurchaseResponse();
        response.setId(purchase.getId());
        response.setBuyerUsername(purchase.getBuyer().getUsername());
        response.setProjectId(purchase.getProject().getProjectId());
        response.setProjectName(purchase.getProject().getProjectName());
        response.setAmount(purchase.getAmount());
        response.setPurchaseDate(purchase.getPurchaseDate());
        return response;
    }
}
