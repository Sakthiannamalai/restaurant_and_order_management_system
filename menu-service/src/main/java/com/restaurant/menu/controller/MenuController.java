package com.restaurant.menu.controller;

import com.restaurant.menu.dto.MenuItemDTO;
import com.restaurant.menu.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuController {
    
    private final MenuService menuService;
    
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    
    @PostMapping("/items")
    public ResponseEntity<MenuItemDTO> createMenuItem(@Valid @RequestBody MenuItemDTO menuItemDTO) {
        try {
            MenuItemDTO createdMenuItem = menuService.createMenuItem(menuItemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMenuItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/items")
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
        List<MenuItemDTO> menuItems = menuService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }
    
    @GetMapping("/items/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        Optional<MenuItemDTO> menuItem = menuService.getMenuItemById(id);
        return menuItem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/items/category/{category}")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItemDTO> menuItems = menuService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(menuItems);
    }
    
    @GetMapping("/items/available")
    public ResponseEntity<List<MenuItemDTO>> getAvailableMenuItems() {
        List<MenuItemDTO> menuItems = menuService.getAvailableMenuItems();
        return ResponseEntity.ok(menuItems);
    }
    
    @GetMapping("/items/available/category/{category}")
    public ResponseEntity<List<MenuItemDTO>> getAvailableMenuItemsByCategory(@PathVariable String category) {
        List<MenuItemDTO> menuItems = menuService.getAvailableMenuItemsByCategory(category);
        return ResponseEntity.ok(menuItems);
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = menuService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @PutMapping("/items/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long id, 
                                                   @Valid @RequestBody MenuItemDTO menuItemDTO) {
        try {
            MenuItemDTO updatedMenuItem = menuService.updateMenuItem(id, menuItemDTO);
            return ResponseEntity.ok(updatedMenuItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        try {
            menuService.deleteMenuItem(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
