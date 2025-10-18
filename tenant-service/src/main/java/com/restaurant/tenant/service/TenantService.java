package com.restaurant.tenant.service;

import com.restaurant.tenant.dto.TenantDTO;
import com.restaurant.tenant.entity.Tenant;
import com.restaurant.tenant.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TenantService {
    
    private final TenantRepository tenantRepository;

    @Autowired
    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }
    
    public TenantDTO createTenant(TenantDTO tenantDTO) {
        if (tenantRepository.existsByName(tenantDTO.getName())) {
            throw new RuntimeException("Tenant name already exists!");
        }
        
        if (tenantRepository.existsBySchemaName(tenantDTO.getSchemaName())) {
            throw new RuntimeException("Schema name already exists!");
        }
        
        Tenant tenant = new Tenant();
        tenant.setName(tenantDTO.getName());
        tenant.setSchemaName(tenantDTO.getSchemaName());
        tenant.setPlan(tenantDTO.getPlan() != null ? tenantDTO.getPlan() : Tenant.Plan.BASIC);
        tenant.setStatus(tenantDTO.getStatus() != null ? tenantDTO.getStatus() : Tenant.Status.ACTIVE);
        
        Tenant savedTenant = tenantRepository.save(tenant);
        
        return new TenantDTO(savedTenant);
    }
    
    public List<TenantDTO> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(TenantDTO::new)
                .collect(Collectors.toList());
    }
    
    public Optional<TenantDTO> getTenantById(Long id) {
        return tenantRepository.findById(id)
                .map(TenantDTO::new);
    }
    
    public Optional<TenantDTO> getTenantByName(String name) {
        return tenantRepository.findByName(name)
                .map(TenantDTO::new);
    }
    
    public TenantDTO updateTenant(Long id, TenantDTO tenantDTO) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found!"));
        
        tenant.setName(tenantDTO.getName());
        tenant.setPlan(tenantDTO.getPlan());
        tenant.setStatus(tenantDTO.getStatus());
        
        Tenant updatedTenant = tenantRepository.save(tenant);
        return new TenantDTO(updatedTenant);
    }
    
    public void deleteTenant(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found!"));
        tenantRepository.deleteById(id);
    }
}

