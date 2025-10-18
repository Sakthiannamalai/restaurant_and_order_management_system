package com.restaurant.kitchen.service;

import com.restaurant.kitchen.dto.KitchenOrderDTO;
import com.restaurant.kitchen.entity.KitchenOrder;
import com.restaurant.kitchen.repository.KitchenOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class KitchenService {
    
    private final KitchenOrderRepository kitchenOrderRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    public KitchenService(KitchenOrderRepository kitchenOrderRepository, 
                         SimpMessagingTemplate messagingTemplate) {
        this.kitchenOrderRepository = kitchenOrderRepository;
        this.messagingTemplate = messagingTemplate;
    }
    
    public KitchenOrderDTO createKitchenOrder(KitchenOrderDTO kitchenOrderDTO) {
        // Check if kitchen order already exists for this order
        if (kitchenOrderRepository.findByOrderId(kitchenOrderDTO.getOrderId()).isPresent()) {
            throw new RuntimeException("Kitchen order already exists for this order!");
        }
        
        KitchenOrder kitchenOrder = new KitchenOrder();
        kitchenOrder.setOrderId(kitchenOrderDTO.getOrderId());
        kitchenOrder.setStatus(kitchenOrderDTO.getStatus() != null ? 
                              kitchenOrderDTO.getStatus() : KitchenOrder.KitchenOrderStatus.PENDING);
        
        KitchenOrder savedKitchenOrder = kitchenOrderRepository.save(kitchenOrder);
        
        // Send WebSocket notification
        sendKitchenOrderUpdate(savedKitchenOrder);
        
        return new KitchenOrderDTO(savedKitchenOrder);
    }
    
    public List<KitchenOrderDTO> getAllKitchenOrders() {
        return kitchenOrderRepository.findAll().stream()
                .map(KitchenOrderDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<KitchenOrderDTO> getKitchenOrdersByStatus(KitchenOrder.KitchenOrderStatus status) {
        return kitchenOrderRepository.findByStatus(status).stream()
                .map(KitchenOrderDTO::new)
                .collect(Collectors.toList());
    }
    
    public Optional<KitchenOrderDTO> getKitchenOrderById(Long id) {
        return kitchenOrderRepository.findById(id)
                .map(KitchenOrderDTO::new);
    }
    
    public Optional<KitchenOrderDTO> getKitchenOrderByOrderId(Long orderId) {
        return kitchenOrderRepository.findByOrderId(orderId)
                .map(KitchenOrderDTO::new);
    }
    
    public KitchenOrderDTO updateKitchenOrderStatus(Long id, KitchenOrder.KitchenOrderStatus status) {
        KitchenOrder kitchenOrder = kitchenOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitchen order not found!"));
        
        kitchenOrder.setStatus(status);
        KitchenOrder updatedKitchenOrder = kitchenOrderRepository.save(kitchenOrder);
        
        // Send WebSocket notification
        sendKitchenOrderUpdate(updatedKitchenOrder);
        
        return new KitchenOrderDTO(updatedKitchenOrder);
    }
    
    public KitchenOrderDTO updateKitchenOrder(Long id, KitchenOrderDTO kitchenOrderDTO) {
        KitchenOrder kitchenOrder = kitchenOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kitchen order not found!"));
        
        kitchenOrder.setStatus(kitchenOrderDTO.getStatus());
        KitchenOrder updatedKitchenOrder = kitchenOrderRepository.save(kitchenOrder);
        
        // Send WebSocket notification
        sendKitchenOrderUpdate(updatedKitchenOrder);
        
        return new KitchenOrderDTO(updatedKitchenOrder);
    }
    
    public void deleteKitchenOrder(Long id) {
        if (!kitchenOrderRepository.existsById(id)) {
            throw new RuntimeException("Kitchen order not found!");
        }
        kitchenOrderRepository.deleteById(id);
    }
    
    private void sendKitchenOrderUpdate(KitchenOrder kitchenOrder) {
        KitchenOrderDTO kitchenOrderDTO = new KitchenOrderDTO(kitchenOrder);
        messagingTemplate.convertAndSend("/topic/kitchen-orders", kitchenOrderDTO);
    }
}
