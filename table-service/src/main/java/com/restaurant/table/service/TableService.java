package com.restaurant.table.service;

import com.restaurant.table.dto.TableDTO;
import com.restaurant.table.entity.Table;
import com.restaurant.table.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {
    
    private final TableRepository tableRepository;
    
    @Autowired
    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }
    
    public TableDTO createTable(TableDTO tableDTO) {
        if (tableRepository.existsByNumber(tableDTO.getNumber())) {
            throw new RuntimeException("Table number already exists!");
        }
        
        Table table = new Table();
        table.setNumber(tableDTO.getNumber());
        table.setCapacity(tableDTO.getCapacity());
        table.setAvailable(tableDTO.getAvailable() != null ? tableDTO.getAvailable() : true);
        
        Table savedTable = tableRepository.save(table);
        return new TableDTO(savedTable);
    }
    
    public List<TableDTO> getAllTables() {
        return tableRepository.findAll().stream()
                .map(TableDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<TableDTO> getAvailableTables() {
        return tableRepository.findByAvailable(true).stream()
                .map(TableDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<TableDTO> getTablesByCapacity(Integer minCapacity) {
        return tableRepository.findByCapacityGreaterThanEqual(minCapacity).stream()
                .map(TableDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<TableDTO> getAvailableTablesByCapacity(Integer minCapacity) {
        return tableRepository.findByAvailableAndCapacityGreaterThanEqual(true, minCapacity).stream()
                .map(TableDTO::new)
                .collect(Collectors.toList());
    }
    
    public Optional<TableDTO> getTableById(Long id) {
        return tableRepository.findById(id)
                .map(TableDTO::new);
    }
    
    public Optional<TableDTO> getTableByNumber(String number) {
        return tableRepository.findByNumber(number)
                .map(TableDTO::new);
    }
    
    public TableDTO updateTable(Long id, TableDTO tableDTO) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found!"));
        
        // Check if the new number conflicts with existing tables
        if (!table.getNumber().equals(tableDTO.getNumber()) && 
            tableRepository.existsByNumber(tableDTO.getNumber())) {
            throw new RuntimeException("Table number already exists!");
        }
        
        table.setNumber(tableDTO.getNumber());
        table.setCapacity(tableDTO.getCapacity());
        table.setAvailable(tableDTO.getAvailable());
        
        Table updatedTable = tableRepository.save(table);
        return new TableDTO(updatedTable);
    }
    
    public void deleteTable(Long id) {
        if (!tableRepository.existsById(id)) {
            throw new RuntimeException("Table not found!");
        }
        tableRepository.deleteById(id);
    }
    
    public TableDTO reserveTable(Long id) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found!"));
        
        if (!table.getAvailable()) {
            throw new RuntimeException("Table is already reserved!");
        }
        
        table.setAvailable(false);
        Table updatedTable = tableRepository.save(table);
        return new TableDTO(updatedTable);
    }
    
    public TableDTO releaseTable(Long id) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found!"));
        
        table.setAvailable(true);
        Table updatedTable = tableRepository.save(table);
        return new TableDTO(updatedTable);
    }
}
