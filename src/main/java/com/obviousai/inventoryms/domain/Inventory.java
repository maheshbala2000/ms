package com.obviousai.inventoryms.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.google.gson.Strictness;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Data;

@Entity
@Data
public class Inventory {
    @Id
    private String id;
    private String productId;
    
    private Integer quantity;
    
    private LocalDateTime lastUpdated;

    @Version
    private Integer version;
}
