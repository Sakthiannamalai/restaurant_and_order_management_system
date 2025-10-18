package com.restaurant.tenant.dto;

import com.restaurant.tenant.entity.Tenant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TenantDTO {
    
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @NotBlank
    @Size(max = 50)
    private String schemaName;
    
    private Tenant.Plan plan;
    private Tenant.Status status;
    
    public TenantDTO() {}
    
    public TenantDTO(Tenant tenant) {
        this.id = tenant.getId();
        this.name = tenant.getName();
        this.schemaName = tenant.getSchemaName();
        this.plan = tenant.getPlan();
        this.status = tenant.getStatus();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSchemaName() {
        return schemaName;
    }
    
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
    
    public Tenant.Plan getPlan() {
        return plan;
    }
    
    public void setPlan(Tenant.Plan plan) {
        this.plan = plan;
    }
    
    public Tenant.Status getStatus() {
        return status;
    }
    
    public void setStatus(Tenant.Status status) {
        this.status = status;
    }
}

