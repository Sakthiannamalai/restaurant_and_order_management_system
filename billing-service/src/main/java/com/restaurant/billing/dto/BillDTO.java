package com.restaurant.billing.dto;

import com.restaurant.billing.entity.Bill;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BillDTO {
    
    private Long id;
    
    @NotNull
    private Long orderId;
    
    @DecimalMin(value = "0.0")
    private BigDecimal total;
    
    private Bill.PaymentMethod paymentMethod;
    private Bill.BillStatus status;
    
    public BillDTO() {}
    
    public BillDTO(Bill bill) {
        this.id = bill.getId();
        this.orderId = bill.getOrderId();
        this.total = bill.getTotal();
        this.paymentMethod = bill.getPaymentMethod();
        this.status = bill.getStatus();
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
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public Bill.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(Bill.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public Bill.BillStatus getStatus() {
        return status;
    }
    
    public void setStatus(Bill.BillStatus status) {
        this.status = status;
    }
}
