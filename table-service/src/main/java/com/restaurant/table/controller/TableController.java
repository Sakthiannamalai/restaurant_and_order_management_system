package com.restaurant.table.controller;

import com.restaurant.table.dto.TableDTO;
import com.restaurant.table.service.TableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "*")
public class TableController {
    
    private final TableService tableService;
    
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }
    
    @PostMapping
    public ResponseEntity<TableDTO> createTable(@Valid @RequestBody TableDTO tableDTO) {
        try {
            TableDTO createdTable = tableService.createTable(tableDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTable);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<TableDTO>> getAllTables() {
        List<TableDTO> tables = tableService.getAllTables();
        return ResponseEntity.ok(tables);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TableDTO> getTableById(@PathVariable Long id) {
        Optional<TableDTO> table = tableService.getTableById(id);
        return table.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/number/{number}")
    public ResponseEntity<TableDTO> getTableByNumber(@PathVariable String number) {
        Optional<TableDTO> table = tableService.getTableByNumber(number);
        return table.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<TableDTO>> getAvailableTables() {
        List<TableDTO> tables = tableService.getAvailableTables();
        return ResponseEntity.ok(tables);
    }
    
    @GetMapping("/capacity/{minCapacity}")
    public ResponseEntity<List<TableDTO>> getTablesByCapacity(@PathVariable Integer minCapacity) {
        List<TableDTO> tables = tableService.getTablesByCapacity(minCapacity);
        return ResponseEntity.ok(tables);
    }
    
    @GetMapping("/available/capacity/{minCapacity}")
    public ResponseEntity<List<TableDTO>> getAvailableTablesByCapacity(@PathVariable Integer minCapacity) {
        List<TableDTO> tables = tableService.getAvailableTablesByCapacity(minCapacity);
        return ResponseEntity.ok(tables);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TableDTO> updateTable(@PathVariable Long id, 
                                             @Valid @RequestBody TableDTO tableDTO) {
        try {
            TableDTO updatedTable = tableService.updateTable(id, tableDTO);
            return ResponseEntity.ok(updatedTable);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/reserve")
    public ResponseEntity<TableDTO> reserveTable(@PathVariable Long id) {
        try {
            TableDTO reservedTable = tableService.reserveTable(id);
            return ResponseEntity.ok(reservedTable);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/release")
    public ResponseEntity<TableDTO> releaseTable(@PathVariable Long id) {
        try {
            TableDTO releasedTable = tableService.releaseTable(id);
            return ResponseEntity.ok(releasedTable);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        try {
            tableService.deleteTable(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
