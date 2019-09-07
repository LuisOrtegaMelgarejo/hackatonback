package com.oip.helpdesk.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oip.helpdesk.domain.entities.Role;


@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {
	
}
