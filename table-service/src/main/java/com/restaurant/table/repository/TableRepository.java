package com.restaurant.table.repository;

import com.restaurant.table.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {
    Optional<Table> findByNumber(String number);
    List<Table> findByAvailable(Boolean available);
    List<Table> findByCapacityGreaterThanEqual(Integer minCapacity);
    List<Table> findByAvailableAndCapacityGreaterThanEqual(Boolean available, Integer minCapacity);
    boolean existsByNumber(String number);
}
