package com.oip.helpdesk.repository;

import com.oip.helpdesk.domain.entities.ConfigurationItem;
import com.oip.helpdesk.domain.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

;

@Repository
public interface ConfigurationItemRepository extends JpaRepository<ConfigurationItem, Long>{

}
