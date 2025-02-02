package com.obviousai.inventoryms.repository;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.obviousai.inventoryms.domain.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {
    
    // Custom query method to find inventory by productId
    Optional<Inventory> findByProductId(String productId);
}
