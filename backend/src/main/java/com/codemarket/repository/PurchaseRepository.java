package com.codemarket.repository;

import com.codemarket.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Page<Purchase> findByBuyer_Username(String username, Pageable pageable);
    Page<Purchase> findByProject_Owner_Username(String username, Pageable pageable);
    Optional<Purchase> findByBuyer_UsernameAndProject_ProjectId(String username, Long projectId);
}
