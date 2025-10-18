package com.restaurant.table.dto;

import com.restaurant.table.entity.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TableDTO {
    
    private Long id;
    
    @NotBlank
    @Size(max = 10)
    private String number;
    
    @Min(value = 1)
    private Integer capacity;
    
    private Boolean available;
    
    public TableDTO() {}
    
    public TableDTO(Table table) {
        this.id = table.getId();
        this.number = table.getNumber();
        this.capacity = table.getCapacity();
        this.available = table.getAvailable();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNumber() {
        return number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public Boolean getAvailable() {
        return available;
    }
    
    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
