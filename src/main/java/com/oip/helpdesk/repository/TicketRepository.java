package com.oip.helpdesk.repository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.oip.helpdesk.domain.entities.DataReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oip.helpdesk.domain.entities.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{
	
	//Page<Ticket> findByUserId(Long userId, Pageable pageable);
	List<Ticket> findByUserId(Long userId);

	@Query(
			value = "SELECT * FROM ticket WHERE assigned_to = ?1",
			nativeQuery = true)
	List<Ticket> findAssignedTickets(Long userId);

	@Query(
			value = "SELECT * FROM ticket WHERE assigned_to is NULL",
			nativeQuery = true)
	List<Ticket> findNullTickets();

	@Query(
			value = "SELECT * FROM ticket WHERE id = ?1",
			nativeQuery = true)
	Ticket findBySpecificId(Long id);

}

