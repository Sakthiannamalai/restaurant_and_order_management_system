package com.restaurant.order.service;

import com.restaurant.order.dto.OrderDTO;
import com.restaurant.order.dto.OrderItemDTO;
import com.restaurant.order.entity.Order;
import com.restaurant.order.entity.OrderItem;
import com.restaurant.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setTableId(orderDTO.getTableId());
        order.setOrderType(orderDTO.getOrderType() != null ? orderDTO.getOrderType() : Order.OrderType.DINE_IN);
        order.setStatus(Order.OrderStatus.PENDING);
        
        Order savedOrder = orderRepository.save(order);
        
        // Calculate total amount if order items are provided
        if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
            BigDecimal totalAmount = calculateTotalAmount(orderDTO.getOrderItems());
            savedOrder.setTotalAmount(totalAmount);
            savedOrder = orderRepository.save(savedOrder);
        }
        
        return new OrderDTO(savedOrder);
    }
    
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<OrderDTO> getOrdersByTable(Long tableId) {
        return orderRepository.findByTableId(tableId).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<OrderDTO> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<OrderDTO> getOrdersByType(Order.OrderType orderType) {
        return orderRepository.findByOrderType(orderType).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }
    
    public Optional<OrderDTO> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(OrderDTO::new);
    }
    
    public OrderDTO updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return new OrderDTO(updatedOrder);
    }
    
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        
        order.setTableId(orderDTO.getTableId());
        order.setOrderType(orderDTO.getOrderType());
        order.setStatus(orderDTO.getStatus());
        
        // Recalculate total amount if order items are provided
        if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
            BigDecimal totalAmount = calculateTotalAmount(orderDTO.getOrderItems());
            order.setTotalAmount(totalAmount);
        }
        
        Order updatedOrder = orderRepository.save(order);
        return new OrderDTO(updatedOrder);
    }
    
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found!");
        }
        orderRepository.deleteById(id);
    }
    
    private BigDecimal calculateTotalAmount(List<OrderItemDTO> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
