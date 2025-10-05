package com.mechanicondemand.repository;

import com.mechanicondemand.entity.MechanicService;
import com.mechanicondemand.entity.ServiceCategory;
import com.mechanicondemand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MechanicServiceRepository - Data Access Layer for MechanicService entity
 */
@Repository
public interface MechanicServiceRepository extends JpaRepository<MechanicService, Long> {
    
    /**
     * Find services by mechanic
     */
    List<MechanicService> findByMechanicOrderByServiceName(User mechanic);
    
    /**
     * Find available services by mechanic
     */
    List<MechanicService> findByMechanicAndIsAvailableTrueOrderByServiceName(User mechanic);
    
    /**
     * Find services by category
     */
    List<MechanicService> findByCategoryOrderByServiceName(ServiceCategory category);
    
    /**
     * Find available services by category
     */
    List<MechanicService> findByCategoryAndIsAvailableTrueOrderByServiceName(ServiceCategory category);
    
    /**
     * Find services by mechanic and category
     */
    List<MechanicService> findByMechanicAndCategoryOrderByServiceName(User mechanic, ServiceCategory category);
    
    /**
     * Find services by name containing (search functionality)
     */
    @Query("SELECT ms FROM MechanicService ms WHERE ms.serviceName LIKE %:name% AND ms.isAvailable = true")
    List<MechanicService> findServicesByNameContaining(@Param("name") String name);
    
    /**
     * Find services by price range
     */
    @Query("SELECT ms FROM MechanicService ms WHERE ms.basePrice BETWEEN :minPrice AND :maxPrice AND ms.isAvailable = true")
    List<MechanicService> findServicesByPriceRange(@Param("minPrice") Double minPrice, 
                                                  @Param("maxPrice") Double maxPrice);
}