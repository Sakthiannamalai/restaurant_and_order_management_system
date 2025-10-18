package com.restaurant.billing.controller;

import com.restaurant.billing.dto.BillDTO;
import com.restaurant.billing.entity.Bill;
import com.restaurant.billing.service.BillingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {
    
    private final BillingService billingService;
    
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }
    
    @PostMapping("/bills")
    public ResponseEntity<BillDTO> createBill(@Valid @RequestBody BillDTO billDTO) {
        try {
            BillDTO createdBill = billingService.createBill(billDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/bills")
    public ResponseEntity<List<BillDTO>> getAllBills() {
        List<BillDTO> bills = billingService.getAllBills();
        return ResponseEntity.ok(bills);
    }
    
    @GetMapping("/bills/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        Optional<BillDTO> bill = billingService.getBillById(id);
        return bill.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/bills/order/{orderId}")
    public ResponseEntity<BillDTO> getBillByOrderId(@PathVariable Long orderId) {
        Optional<BillDTO> bill = billingService.getBillByOrderId(orderId);
        return bill.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/bills/status/{status}")
    public ResponseEntity<List<BillDTO>> getBillsByStatus(@PathVariable Bill.BillStatus status) {
        List<BillDTO> bills = billingService.getBillsByStatus(status);
        return ResponseEntity.ok(bills);
    }
    
    @GetMapping("/bills/payment-method/{paymentMethod}")
    public ResponseEntity<List<BillDTO>> getBillsByPaymentMethod(@PathVariable Bill.PaymentMethod paymentMethod) {
        List<BillDTO> bills = billingService.getBillsByPaymentMethod(paymentMethod);
        return ResponseEntity.ok(bills);
    }
    
    @PutMapping("/bills/{id}")
    public ResponseEntity<BillDTO> updateBill(@PathVariable Long id, 
                                            @Valid @RequestBody BillDTO billDTO) {
        try {
            BillDTO updatedBill = billingService.updateBill(id, billDTO);
            return ResponseEntity.ok(updatedBill);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/bills/{id}/status/{status}")
    public ResponseEntity<BillDTO> updateBillStatus(@PathVariable Long id, 
                                                  @PathVariable Bill.BillStatus status) {
        try {
            BillDTO updatedBill = billingService.updateBillStatus(id, status);
            return ResponseEntity.ok(updatedBill);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/bills/{id}/pay")
    public ResponseEntity<BillDTO> processPayment(@PathVariable Long id, 
                                                @RequestParam Bill.PaymentMethod paymentMethod) {
        try {
            BillDTO paidBill = billingService.processPayment(id, paymentMethod);
            return ResponseEntity.ok(paidBill);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/bills/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        try {
            billingService.deleteBill(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
