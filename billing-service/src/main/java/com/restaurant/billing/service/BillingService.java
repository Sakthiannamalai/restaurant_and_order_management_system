package com.restaurant.billing.service;

import com.restaurant.billing.dto.BillDTO;
import com.restaurant.billing.entity.Bill;
import com.restaurant.billing.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillingService {
    
    private final BillRepository billRepository;
    
    @Autowired
    public BillingService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }
    
    public BillDTO createBill(BillDTO billDTO) {
        // Check if bill already exists for this order
        if (billRepository.findByOrderId(billDTO.getOrderId()).isPresent()) {
            throw new RuntimeException("Bill already exists for this order!");
        }
        
        Bill bill = new Bill();
        bill.setOrderId(billDTO.getOrderId());
        bill.setTotal(billDTO.getTotal());
        bill.setPaymentMethod(billDTO.getPaymentMethod());
        bill.setStatus(billDTO.getStatus() != null ? billDTO.getStatus() : Bill.BillStatus.PENDING);
        
        Bill savedBill = billRepository.save(bill);
        return new BillDTO(savedBill);
    }
    
    public List<BillDTO> getAllBills() {
        return billRepository.findAll().stream()
                .map(BillDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<BillDTO> getBillsByStatus(Bill.BillStatus status) {
        return billRepository.findByStatus(status).stream()
                .map(BillDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<BillDTO> getBillsByPaymentMethod(Bill.PaymentMethod paymentMethod) {
        return billRepository.findByPaymentMethod(paymentMethod).stream()
                .map(BillDTO::new)
                .collect(Collectors.toList());
    }
    
    public Optional<BillDTO> getBillById(Long id) {
        return billRepository.findById(id)
                .map(BillDTO::new);
    }
    
    public Optional<BillDTO> getBillByOrderId(Long orderId) {
        return billRepository.findByOrderId(orderId)
                .map(BillDTO::new);
    }
    
    public BillDTO updateBillStatus(Long id, Bill.BillStatus status) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found!"));
        
        bill.setStatus(status);
        Bill updatedBill = billRepository.save(bill);
        return new BillDTO(updatedBill);
    }
    
    public BillDTO updateBill(Long id, BillDTO billDTO) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found!"));
        
        bill.setTotal(billDTO.getTotal());
        bill.setPaymentMethod(billDTO.getPaymentMethod());
        bill.setStatus(billDTO.getStatus());
        
        Bill updatedBill = billRepository.save(bill);
        return new BillDTO(updatedBill);
    }
    
    public void deleteBill(Long id) {
        if (!billRepository.existsById(id)) {
            throw new RuntimeException("Bill not found!");
        }
        billRepository.deleteById(id);
    }
    
    public BillDTO processPayment(Long billId, Bill.PaymentMethod paymentMethod) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found!"));
        
        bill.setPaymentMethod(paymentMethod);
        bill.setStatus(Bill.BillStatus.PAID);
        
        Bill updatedBill = billRepository.save(bill);
        return new BillDTO(updatedBill);
    }
}
