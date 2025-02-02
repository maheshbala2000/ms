package com.obviousai.inventoryms.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.obviousai.inventoryms.domain.Inventory;
import com.obviousai.inventoryms.dtos.InventoryDTO;
import com.obviousai.inventoryms.exceptions.ResourceNotFoundException;
import com.obviousai.inventoryms.service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Inventory> addOrUpdateStock(@RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryService.addOrUpdateStock(inventory));
    }

    @GetMapping("/{productId}")
    public InventoryDTO getStockDetails(@PathVariable UUID productId) {
        return inventoryService.getStockDetails(productId);
    }

    @PostMapping(path="/validate/{productId}", produces = "application/json")
    public ResponseEntity validateAndLockStock(@PathVariable String productId) {
    	UUID uuid = UUID.fromString(productId);
    	InventoryDTO invObj = null;
    	ResponseEntity res = null;
    	try {
			invObj = inventoryService.validateAndLockStock(uuid);
			res  = new ResponseEntity(invObj, HttpStatus.OK);
		} 
    	catch (ResourceNotFoundException|NullPointerException e) {
    		// Saga: Rollback lockstock
			res = new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
    	catch (Exception e) {
			res = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
        return res;
    }
}
