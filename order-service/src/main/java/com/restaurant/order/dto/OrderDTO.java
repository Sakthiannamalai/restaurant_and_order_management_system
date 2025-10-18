package com.restaurant.order.dto;

import com.restaurant.order.entity.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    
    private Long id;
    private Long tableId;
    private Order.OrderStatus status;
    private BigDecimal totalAmount;
    private Order.OrderType orderType;
    
    @Valid
    private List<OrderItemDTO> orderItems;
    
    public OrderDTO() {}
    
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.tableId = order.getTableId();
        this.status = order.getStatus();
        this.totalAmount = order.getTotalAmount();
        this.orderType = order.getOrderType();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getTableId() {
        return tableId;
    }
    
    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
    
    public Order.OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(Order.OrderStatus status) {
        this.status = status;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public Order.OrderType getOrderType() {
        return orderType;
    }
    
    public void setOrderType(Order.OrderType orderType) {
        this.orderType = orderType;
    }
    
    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
