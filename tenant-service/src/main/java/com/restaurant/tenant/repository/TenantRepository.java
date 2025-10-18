package com.restaurant.tenant.repository;

import com.restaurant.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByName(String name);
    Optional<Tenant> findBySchemaName(String schemaName);
    boolean existsByName(String name);
    boolean existsBySchemaName(String schemaName);
}

