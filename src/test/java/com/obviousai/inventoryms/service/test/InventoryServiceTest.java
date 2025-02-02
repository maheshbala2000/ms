package com.obviousai.inventoryms.service.test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.annotations.processing.SQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.obviousai.inventoryms.domain.Inventory;
import com.obviousai.inventoryms.dtos.InventoryDTO;
import com.obviousai.inventoryms.exceptions.InsufficientInventoryException;
import com.obviousai.inventoryms.exceptions.ResourceNotFoundException;
import com.obviousai.inventoryms.repository.InventoryRepository;
import com.obviousai.inventoryms.service.InventoryService;

import junit.framework.TestCase;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest extends TestCase{

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ModelMapper modelMapper;

    private Inventory inventory;
    private InventoryDTO inventoryDTO;

    @BeforeEach
	protected void setUp() {
        inventory = new Inventory();
        inventory.setProductId("223e4567-e89b-12d3-a456-426614174004");
        inventory.setQuantity(10);
        inventory.setLastUpdated(LocalDateTime.now());
    
        inventoryDTO = new InventoryDTO();
        // Set properties of inventoryDTO as needed
        inventoryDTO.setProductId("223e4567-e89b-12d3-a456-426614174004");
        inventoryDTO.setQuantity(18);
        
    }
    
    @Test
    void testAddOrUpdateStock() {
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Inventory savedInventory = inventoryService.addOrUpdateStock(inventory);

        assertNotNull(savedInventory);
        assertEquals(inventory.getProductId(), savedInventory.getProductId());
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    void testGetStockDetails_Success() {
        UUID productId = UUID.fromString(inventory.getProductId());
        when(inventoryRepository.findByProductId(eq(productId.toString()))).thenReturn(Optional.of(inventory));
        when(modelMapper.map(inventory, InventoryDTO.class)).thenReturn(inventoryDTO);

        InventoryDTO result = inventoryService.getStockDetails(productId);

        assertNotNull(result);
        verify(inventoryRepository, times(1)).findByProductId(eq(productId.toString()));
        verify(modelMapper, times(1)).map(inventory, InventoryDTO.class);
    }

    @Test
    void testGetStockDetails_ProductNotFound() {
        UUID productId = UUID.randomUUID();
        when(inventoryRepository.findByProductId(eq(productId.toString()))).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            inventoryService.getStockDetails(productId);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testValidateAndLockStock_Success() {
        UUID productId = UUID.fromString(inventory.getProductId());
        when(inventoryRepository.findByProductId(eq(productId.toString()))).thenReturn(Optional.of(inventory));
        when(modelMapper.map(any(), eq(InventoryDTO.class))).thenReturn(inventoryDTO);

        InventoryDTO result = inventoryService.validateAndLockStock(productId);

        assertNotNull(result);
        assertEquals((int)9, (int)inventory.getQuantity()); // Quantity should be decremented
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }
    

    @Test
    void testValidateAndLockStock_InsufficientInventory() {
        inventory.setQuantity(0); // Set quantity to 0 to simulate insufficient stock
        UUID productId = UUID.fromString(inventory.getProductId());
        
        when(inventoryRepository.findByProductId(eq(productId.toString()))).thenReturn(Optional.of(inventory));

        Exception exception = assertThrows(InsufficientInventoryException.class, () -> {
            inventoryService.validateAndLockStock(productId);
        });

        assertEquals("Product inventory not sufficient to meet demand", exception.getMessage());
    }

    @Test
    void testValidateAndLockStock_ProductNotFound() {
        UUID productId = UUID.randomUUID();
        
        when(inventoryRepository.findByProductId(eq(productId.toString()))).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            inventoryService.validateAndLockStock(productId);
        });

        assertEquals("Product not found", exception.getMessage());
    }

}
