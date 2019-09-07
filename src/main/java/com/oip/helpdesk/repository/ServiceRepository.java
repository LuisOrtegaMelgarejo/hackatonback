package com.oip.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oip.helpdesk.domain.entities.Service;
;import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long>{
    @Query(
            value = "SELECT * FROM service WHERE configuration_item_id = ?1",
            nativeQuery = true)
    List<Service> findByConfigurationItemId(Long id);
}
