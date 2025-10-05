package com.mechanicondemand.repository;

import com.mechanicondemand.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ServiceCategoryRepository - Data Access Layer for ServiceCategory entity
 */
@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
    
    /**
     * Find active service categories
     */
    List<ServiceCategory> findByIsActiveTrueOrderByName();
    
    /**
     * Find service category by name
     */
    ServiceCategory findByName(String name);
}