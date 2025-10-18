package com.restaurant.menu.service;

import com.restaurant.menu.dto.MenuItemDTO;
import com.restaurant.menu.entity.MenuItem;
import com.restaurant.menu.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    
    private final MenuItemRepository menuItemRepository;
    
    @Autowired
    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }
    
    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemDTO.getName());
        menuItem.setDescription(menuItemDTO.getDescription());
        menuItem.setCategory(menuItemDTO.getCategory());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setAvailable(menuItemDTO.getAvailable() != null ? menuItemDTO.getAvailable() : true);
        
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return new MenuItemDTO(savedMenuItem);
    }
    
    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(MenuItemDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<MenuItemDTO> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategory(category).stream()
                .map(MenuItemDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<MenuItemDTO> getAvailableMenuItems() {
        return menuItemRepository.findByAvailable(true).stream()
                .map(MenuItemDTO::new)
                .collect(Collectors.toList());
    }
    
    public List<MenuItemDTO> getAvailableMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategoryAndAvailable(category, true).stream()
                .map(MenuItemDTO::new)
                .collect(Collectors.toList());
    }
    
    public Optional<MenuItemDTO> getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .map(MenuItemDTO::new);
    }
    
    public MenuItemDTO updateMenuItem(Long id, MenuItemDTO menuItemDTO) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found!"));
        
        menuItem.setName(menuItemDTO.getName());
        menuItem.setDescription(menuItemDTO.getDescription());
        menuItem.setCategory(menuItemDTO.getCategory());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setAvailable(menuItemDTO.getAvailable());
        
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return new MenuItemDTO(updatedMenuItem);
    }
    
    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new RuntimeException("Menu item not found!");
        }
        menuItemRepository.deleteById(id);
    }
    
    public List<String> getAllCategories() {
        return menuItemRepository.findAll().stream()
                .map(MenuItem::getCategory)
                .distinct()
                .filter(category -> category != null && !category.isEmpty())
                .collect(Collectors.toList());
    }
}

