package com.restaurant.billing.repository;

import com.restaurant.billing.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByOrderId(Long orderId);
    List<Bill> findByStatus(Bill.BillStatus status);
    List<Bill> findByPaymentMethod(Bill.PaymentMethod paymentMethod);
}
