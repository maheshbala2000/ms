package com.obviousai.inventoryms.dtos;

import lombok.Data;

@Data
public class InventoryDTO {
    private String id; // UUID as String
    private String productId; // UUID as String
    private Integer quantity;

    // Constructors, Getters, and Setters
}
