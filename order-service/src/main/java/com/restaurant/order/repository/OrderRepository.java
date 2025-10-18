package com.restaurant.order.repository;

import com.restaurant.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTableId(Long tableId);
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByOrderType(Order.OrderType orderType);
    List<Order> findByTableIdAndStatus(Long tableId, Order.OrderStatus status);
}
