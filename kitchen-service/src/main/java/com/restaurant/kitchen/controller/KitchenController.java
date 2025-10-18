package com.restaurant.kitchen.controller;

import com.restaurant.kitchen.dto.KitchenOrderDTO;
import com.restaurant.kitchen.entity.KitchenOrder;
import com.restaurant.kitchen.service.KitchenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kitchen")
@CrossOrigin(origins = "*")
public class KitchenController {
    
    private final KitchenService kitchenService;
    
    public KitchenController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }
    
    @PostMapping("/orders")
    public ResponseEntity<KitchenOrderDTO> createKitchenOrder(@Valid @RequestBody KitchenOrderDTO kitchenOrderDTO) {
        try {
            KitchenOrderDTO createdKitchenOrder = kitchenService.createKitchenOrder(kitchenOrderDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdKitchenOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/orders")
    public ResponseEntity<List<KitchenOrderDTO>> getAllKitchenOrders() {
        List<KitchenOrderDTO> kitchenOrders = kitchenService.getAllKitchenOrders();
        return ResponseEntity.ok(kitchenOrders);
    }
    
    @GetMapping("/orders/{id}")
    public ResponseEntity<KitchenOrderDTO> getKitchenOrderById(@PathVariable Long id) {
        Optional<KitchenOrderDTO> kitchenOrder = kitchenService.getKitchenOrderById(id);
        return kitchenOrder.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/orders/order/{orderId}")
    public ResponseEntity<KitchenOrderDTO> getKitchenOrderByOrderId(@PathVariable Long orderId) {
        Optional<KitchenOrderDTO> kitchenOrder = kitchenService.getKitchenOrderByOrderId(orderId);
        return kitchenOrder.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<List<KitchenOrderDTO>> getKitchenOrdersByStatus(@PathVariable KitchenOrder.KitchenOrderStatus status) {
        List<KitchenOrderDTO> kitchenOrders = kitchenService.getKitchenOrdersByStatus(status);
        return ResponseEntity.ok(kitchenOrders);
    }
    
    @PutMapping("/orders/{id}")
    public ResponseEntity<KitchenOrderDTO> updateKitchenOrder(@PathVariable Long id, 
                                                            @Valid @RequestBody KitchenOrderDTO kitchenOrderDTO) {
        try {
            KitchenOrderDTO updatedKitchenOrder = kitchenService.updateKitchenOrder(id, kitchenOrderDTO);
            return ResponseEntity.ok(updatedKitchenOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/orders/{id}/status/{status}")
    public ResponseEntity<KitchenOrderDTO> updateKitchenOrderStatus(@PathVariable Long id, 
                                                                   @PathVariable KitchenOrder.KitchenOrderStatus status) {
        try {
            KitchenOrderDTO updatedKitchenOrder = kitchenService.updateKitchenOrderStatus(id, status);
            return ResponseEntity.ok(updatedKitchenOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteKitchenOrder(@PathVariable Long id) {
        try {
            kitchenService.deleteKitchenOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
