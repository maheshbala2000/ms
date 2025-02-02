package com.obviousai.inventoryms.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.obviousai.inventoryms.domain.Inventory;
import com.obviousai.inventoryms.dtos.InventoryDTO;
import com.obviousai.inventoryms.exceptions.InsufficientInventoryException;
import com.obviousai.inventoryms.exceptions.ResourceNotFoundException;
import com.obviousai.inventoryms.repository.InventoryRepository;

import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional
    public Inventory addOrUpdateStock(Inventory inventory) {
        // Implement retry logic here if needed
        return inventoryRepository.save(inventory);
    }
    
    @Autowired
    ModelMapper mapper;
    
    public InventoryDTO getStockDetails(UUID productId) {
    	Inventory inventory = inventoryRepository.findByProductId(productId.toString())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return mapper.map(inventory,InventoryDTO.class);
    }

    @Retry(name = "inventoryRetry")
    @Transactional
    public InventoryDTO validateAndLockStock(UUID productId) throws ResourceNotFoundException{
        // Fetch the inventory item for the given product ID
        Inventory inventory = inventoryRepository.findByProductId(productId.toString())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Check if there is sufficient stock
        if (inventory.getQuantity() == 0) {
            throw new InsufficientInventoryException("Product inventory not sufficient to meet demand");
        }

        // Lock the stock by decrementing the quantity
        inventory.setQuantity(inventory.getQuantity() - 1);
        inventory.setLastUpdated(LocalDateTime.now());

        // Save the updated inventory item
        inventoryRepository.save(inventory);
        
        return mapper.map(inventory,InventoryDTO.class);
    }
}
