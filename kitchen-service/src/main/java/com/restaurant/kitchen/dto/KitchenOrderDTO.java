package com.restaurant.kitchen.dto;

import com.restaurant.kitchen.entity.KitchenOrder;
import jakarta.validation.constraints.NotNull;

public class KitchenOrderDTO {
    
    private Long id;
    
    @NotNull
    private Long orderId;
    
    private KitchenOrder.KitchenOrderStatus status;
    
    public KitchenOrderDTO() {}
    
    public KitchenOrderDTO(KitchenOrder kitchenOrder) {
        this.id = kitchenOrder.getId();
        this.orderId = kitchenOrder.getOrderId();
        this.status = kitchenOrder.getStatus();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public KitchenOrder.KitchenOrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(KitchenOrder.KitchenOrderStatus status) {
        this.status = status;
    }
}
